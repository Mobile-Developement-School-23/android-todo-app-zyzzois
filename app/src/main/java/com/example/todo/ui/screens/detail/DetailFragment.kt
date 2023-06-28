package com.example.todo.ui.screens.detail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity
import com.example.todo.R
import com.example.todo.app.ToDoApp
import com.example.todo.databinding.FragmentDetailBinding
import com.example.todo.ui.screens.main.ViewModelFactory
import com.example.todo.ui.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.todo.ui.util.Constants.DEFAULT_ID
import com.example.todo.ui.util.Constants.MODE_ADD
import com.example.todo.ui.util.Constants.MODE_EDIT
import com.example.todo.ui.util.Constants.MODE_UNKNOWN
import com.example.todo.ui.util.Constants.PARAM_TODO_ITEM_ID_IS_ABSENT_EXCEPTION_MESSAGE
import com.example.todo.ui.util.Constants.PICKER_TAG
import com.example.todo.ui.util.Constants.TODO_DELETED
import com.example.todo.ui.util.Constants.UNKNOWN_SCREEN_MODE
import com.example.todo.ui.util.Converter
import com.example.todo.ui.util.dateFromLong
import com.example.todo.ui.util.showToast
import com.example.todo.ui.util.toStringDate
import com.google.android.material.datepicker.MaterialDatePicker
import javax.inject.Inject

class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private var screenMode: String = MODE_UNKNOWN
    private var toDoItemEntityId: Int = TodoItemEntity.UNDEFINED_ID
    private var tempValueForDeadline = TodoItemEntity.UNDEFINED_DATE

    private val component by lazy {
        (requireActivity().application as ToDoApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL_EXCEPTION_MESSAGE)

    private val mainPopupMenu by lazy {
        PopupMenu(context, binding.tvImportance)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextChangeListeners()
        launchRightMode()
        setupClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputText.observe(viewLifecycleOwner) {
            val message = if (it) getString(R.string.til_error) else null
            binding.textInputLayout.error = message
        }
    }

    private fun parseParams() {
        val mode = args.mode
        if (mode != MODE_EDIT && mode != MODE_ADD)
            throw RuntimeException(UNKNOWN_SCREEN_MODE + mode)
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (args.todoItemId == DEFAULT_ID)
                throw RuntimeException(PARAM_TODO_ITEM_ID_IS_ABSENT_EXCEPTION_MESSAGE)

            toDoItemEntityId = args.todoItemId
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
            else -> throw RuntimeException(UNKNOWN_SCREEN_MODE + args.mode)
        }
    }

    private fun launchEditMode() = with(binding) {
        buttonDelete.apply {
            isClickable = true
            iconTint = ContextCompat.getColorStateList(requireContext(), R.color.red)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }

        viewModel.getToDoItem(toDoItemEntityId)
        viewModel.itemEntity.observe(viewLifecycleOwner) {
            if (it.deadline != TodoItemEntity.UNDEFINED_DATE) {
                switch1.isChecked = true
                tvDate.visibility = View.VISIBLE
                tvDate.text = dateFromLong(it.deadline).toStringDate()
            }
            editText.setText(it.text)
            when (it.importance) {
                Importance.Low -> {
                    tvImportanceState.text = getString(R.string.low_importance)
                    tvImportanceState.setTextColor(requireContext().getColor(R.color.black))
                }
                Importance.Urgent -> {
                    tvImportanceState.text = getString(R.string.high_importance)
                    tvImportanceState.setTextColor(requireContext().getColor(R.color.red))
                }
                Importance.Normal -> {
                    tvImportanceState.text = getString(R.string.hint_no)
                    tvImportanceState.setTextColor(requireContext().getColor(R.color.medium_gray))
                }
            }
        }

        switch1.setOnClickListener {
            if (switch1.isChecked) {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                picker.apply {
                    addOnPositiveButtonClickListener {
                        tvDate.visibility = View.VISIBLE
                        tvDate.text = dateFromLong(it).toStringDate()
                        tempValueForDeadline = it
                    }
                    addOnCancelListener {
                        switch1.isChecked = false
                        tempValueForDeadline = TodoItemEntity.UNDEFINED_DATE
                    }
                    addOnNegativeButtonClickListener {
                        switch1.isChecked = false
                        tempValueForDeadline = TodoItemEntity.UNDEFINED_DATE
                    }
                }

                picker.show(requireActivity().supportFragmentManager, PICKER_TAG)

            } else {
                tempValueForDeadline = TodoItemEntity.UNDEFINED_DATE
                tvDate.visibility = View.INVISIBLE
            }

        }

        buttonSave.setOnClickListener {
            viewModel.editToDoItem(
                text = editText.text?.toString(),
                importance = Converter.convertStringToImportance(tvImportanceState.text.toString()),
                deadline = tempValueForDeadline
            )

            if (viewModel.errorInputText.value == false) {
                tvDate.visibility = View.INVISIBLE
                findNavController().popBackStack()
            }
        }

        buttonDelete.setOnClickListener {
            viewModel.deleteToDoItem(toDoItemEntityId)
            findNavController().popBackStack()
            showToast(TODO_DELETED)
        }
    }

    private fun launchAddMode() = with(binding) {
        switch1.setOnCheckedChangeListener { _, isChecked ->
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
            picker.addOnPositiveButtonClickListener {
                tvDate.visibility = View.VISIBLE
                tvDate.text = dateFromLong(it).toStringDate()
                tempValueForDeadline = it
            }

            if (isChecked)
                picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
            else
                tvDate.visibility = View.INVISIBLE

        }

        buttonSave.setOnClickListener {
            viewModel.addToDoItem(
                text = editText.text?.toString(),
                importance = tvImportanceState.text.toString(),
                deadline = tempValueForDeadline
            )
            if (viewModel.errorInputText.value == false)
                findNavController().popBackStack()
        }
    }

    private fun setupClickListeners() = with(binding) {

        with(mainPopupMenu) {
            tvImportance.setOnClickListener {
                menu.clear()
                menuInflater.inflate(R.menu.importance_menu, menu)
                with(binding.tvImportanceState) {
                    setOnMenuItemClickListener { item ->
                        if (item != null) {
                            when (item.itemId) {
                                R.id.lowImportance -> {
                                    text = getString(R.string.low_importance)
                                    setTextColor(requireContext().getColor(R.color.black))
                                }
                                R.id.highImportance -> {
                                    text = getString(R.string.high_importance)
                                    setTextColor(requireContext().getColor(R.color.red))
                                }
                                else -> {
                                    text = getString(R.string.hint_no)
                                    setTextColor(requireContext().getColor(R.color.medium_gray))
                                }
                            }
                        }
                        true
                    }
                }
                show()
            }
        }

        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun addTextChangeListeners() {
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputText()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}