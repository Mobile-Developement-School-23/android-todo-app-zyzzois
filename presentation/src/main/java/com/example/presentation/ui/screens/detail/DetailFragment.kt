package com.example.presentation.ui.screens.detail

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.domain.entity.Importance
import com.example.domain.entity.TodoItemEntity.Companion.UNDEFINED_DATE
import com.example.presentation.R
import com.example.presentation.databinding.FragmentDetailBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.ui.core.AlarmReceiver
import com.example.presentation.ui.core.factories.ViewModelFactory
import com.example.presentation.ui.screens.detail.components.AppBar
import com.example.presentation.ui.screens.detail.components.ButtonDelete
import com.example.presentation.ui.screens.detail.components.Importance
import com.example.presentation.ui.screens.detail.components.InputText
import com.example.presentation.ui.screens.detail.components.MyDivider
import com.example.presentation.ui.screens.detail.components.TodoDataPicker
import com.example.presentation.ui.theme.TodoAppCustomTheme
import com.example.presentation.ui.theme.TodoAppTheme
import com.example.presentation.ui.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.presentation.ui.util.Constants.MODE_UNKNOWN
import com.example.presentation.ui.util.Constants.PICKER_TAG
import com.example.presentation.ui.util.Constants.TODO_DELETED
import com.example.presentation.ui.util.Converter
import com.example.presentation.ui.util.dateFromLong
import com.example.presentation.ui.util.getMillisAtMidnight
import com.example.presentation.ui.util.getMillisFromHourAndMinutes
import com.example.presentation.ui.util.showToast
import com.example.presentation.ui.util.toStringDate
import com.example.presentation.ui.util.toTextFormat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import javax.inject.Inject

class DetailFragment : Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private var screenMode = MODE_UNKNOWN


    private val component by lazy {
        (requireActivity().application as PresentationComponentProvider)
            .providePresentationComponent().create()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]
    }

    private lateinit var bottomSheetBehaviorImportance: BottomSheetBehavior<LinearLayout>
    private val vibrator by lazy { requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL_EXCEPTION_MESSAGE)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //parseParams()
    }

    private lateinit var composeView: ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView.setContent {

            TodoAppTheme {
                val item = viewModel.itemEntity.value

                val text = remember {
                    if (item != null)
                        mutableStateOf(item.text)
                    else
                        mutableStateOf("")
                }

                val importanceValue = remember {
                    if (item != null)
                        mutableStateOf(item.importance)
                    else
                        mutableStateOf(Importance.Basic)
                }

                val deadlineValue = remember {
                    if (item != null)
                        mutableLongStateOf(item.deadline)
                    else
                        mutableLongStateOf(UNDEFINED_DATE)
                }
                val deadlineText = remember {
                    if (item != null)
                        mutableStateOf("00:00")
                    else
                        mutableStateOf("00:00")
                }
                val isDateTextVisible = remember {
                    if (item != null && item.deadline != UNDEFINED_DATE)
                        mutableStateOf(true)
                    else
                        mutableStateOf(false)
                }
                val enableClick = remember {
                    if (item != null )
                        mutableStateOf(true)
                    else
                        mutableStateOf(false)
                }
                val listState = rememberLazyListState()

                val topBarElevation by animateDpAsState(
                    if(listState.canScrollBackward) 8.dp else 0.dp,
                    label = "top bar elevation"
                )

                enableClick.value = text.value.isNotEmpty()

                Scaffold(
                    topBar = {
                        AppBar(enableClick.value, topBarElevation,
                            {
                                viewModel.addToDoItem(
                                    text.value,
                                    importanceValue.value.toString(),
                                    deadlineValue.value
                                )
                                if (deadlineValue.value != UNDEFINED_DATE)
                                    scheduleAlarm(deadlineValue.value, text.value)
                                findNavController().popBackStack()
                            },
                            { findNavController().popBackStack() }
                        )
                    },
                    containerColor = TodoAppCustomTheme.colors.backPrimary
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        state = listState
                    ) {
                        item {

                            InputText(text)
                            Importance(importanceValue)
                            MyDivider(PaddingValues(horizontal = 16.dp))
                            TodoDataPicker(deadlineValue, deadlineText, isDateTextVisible)
                            MyDivider(PaddingValues(horizontal = 16.dp))
                            ButtonDelete(enableClick.value) {
                                viewModel.deleteToDoItem(args.todoItemId)
                                findNavController().popBackStack()
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(96.dp))
                        }
                    }
                }
            }
        }
//        setupBottomSheet()
//        launchRightMode()
//        setupClickListeners()
//        observeViewModel()
    }

//    private fun setupBottomSheet() {
//        bottomSheetBehaviorImportance = BottomSheetBehavior.from(binding.bottomPriorityMenu.priorityMenu)
//        bottomSheetBehaviorImportance.peekHeight = 0
//        bottomSheetBehaviorImportance.state = BottomSheetBehavior.STATE_COLLAPSED
//    }

//    private fun observeViewModel() {
//        viewModel.errorInputText.observe(viewLifecycleOwner) {
//            val message = if (it) getString(R.string.til_error) else null
//            binding.textInputLayout.error = message
//        }
//    }
//
//    private fun parseParams() {
//        val mode = args.mode
//        if (mode != MODE_EDIT && mode != MODE_ADD)
//            throw RuntimeException(UNKNOWN_SCREEN_MODE + mode)
//        screenMode = mode
//
//        if (screenMode == MODE_EDIT) {
//            if (args.todoItemId == DEFAULT_ID)
//                throw RuntimeException(PARAM_TODO_ITEM_ID_IS_ABSENT_EXCEPTION_MESSAGE)
//            viewModel.toDoItemEntityId = args.todoItemId
//        }
//    }
//
//    private fun launchRightMode() {
//        when (screenMode) {
//            MODE_ADD -> launchAddMode()
//            MODE_EDIT -> launchEditMode()
//            else -> throw RuntimeException(UNKNOWN_SCREEN_MODE + args.mode)
//        }
//    }

//    private fun launchEditMode() = with(binding) {
//        showEditingTodo()
//        setupSwitchForEditMode()
//        setupButtonsForEditMode()
//    }

//    private fun showEditingTodo() = with(binding) {
//        viewModel.getToDoItem(viewModel.toDoItemEntityId)
//        viewModel.itemEntity.observe(viewLifecycleOwner) {
//            if (it.deadline != UNDEFINED_DATE) {
//                switch1.isChecked = true
//                tvDate.visibility = View.VISIBLE
//                tvDate.text = dateFromLong(it.deadline).toStringDate()
//            }
//            editText.setText(it.text)
//            when (it.importance) {
//                Importance.Low -> {
//                    tvImportanceState.text = getString(R.string.low_importance)
//                    tvImportanceState.setTextColor(requireContext().getColor(R.color.black))
//                }
//                Importance.Important -> {
//                    tvImportanceState.text = getString(R.string.high_importance)
//                    tvImportanceState.setTextColor(requireContext().getColor(R.color.color_light_red))
//                }
//                Importance.Basic -> {
//                    tvImportanceState.text = getString(R.string.hint_no)
//                    tvImportanceState.setTextColor(requireContext().getColor(R.color.medium_gray))
//                }
//            }
//        }
//    }

    private fun setupButtonsForEditMode() = with(binding) {
        buttonSave.setOnClickListener {
            viewModel.editToDoItem(
                text = editText.text?.toString(),
                importance = Converter.convertStringToImportance(tvImportanceState.text.toString()),
                deadline = viewModel.tempValueForDeadline
            )

            if (viewModel.errorInputText.value == false) {
                tvDate.visibility = View.INVISIBLE
                findNavController().popBackStack()
            }

        }
        buttonDelete.apply {
            isClickable = true
            iconTint = ContextCompat.getColorStateList(requireContext(), R.color.color_light_red)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.color_light_red))
            setOnClickListener {
                vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
                viewModel.deleteToDoItem(viewModel.toDoItemEntityId)
                findNavController().popBackStack()
                showToast(TODO_DELETED)
            }

        }
    }

    private fun setupSwitchForEditMode() = with(binding) {
        switch1.setOnClickListener {
            vibrator.vibrate(VibrationEffect.createOneShot(60, VibrationEffect.DEFAULT_AMPLITUDE))
            if (switch1.isChecked) {
                val picker = MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                picker.apply {
                    addOnPositiveButtonClickListener {
                        tvDate.visibility = View.VISIBLE
                        tvDate.text = dateFromLong(it).toStringDate()
                        viewModel.tempValueForDeadline = it
                    }
                    addOnCancelListener {
                        switch1.isChecked = false
                        viewModel.tempValueForDeadline = UNDEFINED_DATE
                    }
                    addOnNegativeButtonClickListener {
                        switch1.isChecked = false
                        viewModel.tempValueForDeadline = UNDEFINED_DATE
                    }
                }

                picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
            } else {
                viewModel.tempValueForDeadline = UNDEFINED_DATE
                tvDate.visibility = View.INVISIBLE
            }
        }
    }

    private fun launchAddMode() = with(binding) {

        switch1.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .build()

            picker.addOnPositiveButtonClickListener {
                showTimePicker(it.getMillisAtMidnight())
            }

            picker.addOnDismissListener { switch1.isChecked = false }
            picker.addOnCancelListener { switch1.isChecked = false }

            if (switch1.isChecked) picker.show(requireActivity().supportFragmentManager, PICKER_TAG)
            else tvDate.visibility = View.INVISIBLE
        }

        buttonSave.setOnClickListener {
            viewModel.addToDoItem(
                text = editText.text?.toString(),
                importance = tvImportanceState.text.toString(),
                deadline = viewModel.tempValueForDeadline
            )

            if (viewModel.errorInputText.value == false)
                findNavController().popBackStack()
        }
    }

    private fun scheduleAlarm(alarmTime: Long, content: String) {
        val alarmManager = requireContext().applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager

        val receiverIntent = AlarmReceiver.newIntent(requireContext().applicationContext, content)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            100,
            receiverIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
    }

    private fun showTimePicker(date: Long) = with(binding) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H).setHour(12).setMinute(10)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(requireContext().getString(R.string.time_picker_title))
            .build()
        timePicker.show(requireActivity().supportFragmentManager, PICKER_TAG)
        timePicker.addOnNegativeButtonClickListener {
            tvDate.visibility = View.VISIBLE
            tvDate.text = dateFromLong(date).toStringDate()
            viewModel.tempValueForDeadline = date
            switch1.isChecked = true
            showToast("Выполнить до ${date.toTextFormat()}")
        }
        timePicker.addOnPositiveButtonClickListener {
            switch1.isChecked = true
            tvDate.visibility = View.VISIBLE
            val exactTime = getMillisFromHourAndMinutes(timePicker.hour, timePicker.minute) + date
            tvDate.text = dateFromLong(exactTime).toStringDate()
            viewModel.tempValueForDeadline = exactTime
            scheduleAlarm(exactTime, binding.editText.text.toString())
            showToast("Выполнить до ${exactTime.toTextFormat()}")
        }
    }

    private fun setupClickListeners() = with(binding) {
        val importanceMenu = PopupMenu(context, binding.tvImportance)
        importanceMenu.inflate(R.menu.importance_menu)
        tvImportance.setOnClickListener {
//            importanceMenu.show()
            bottomSheetBehaviorImportance.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBackGround.visibility = View.VISIBLE

            bottomSheetBackGround.setOnClickListener {
                hideBottomSheetMenus()
            }

            bottomPriorityMenu.basic.setOnClickListener {
                tvImportanceState.text = getString(R.string.low_importance)
                tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))
                hideBottomSheetMenus()
            }
            bottomPriorityMenu.low.setOnClickListener {
                tvImportanceState.text = getString(R.string.hint_no)
                tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
                hideBottomSheetMenus()
            }
            bottomPriorityMenu.high.setOnClickListener {
                tvImportanceState.text = getString(R.string.high_importance)
                tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_light_red))
                hideBottomSheetMenus()
            }
        }
//        importanceMenu.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.lowImportance -> {
//                    tvImportanceState.text = getString(R.string.low_importance)
//                    tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
//                }
//                R.id.highImportance -> {
//                    tvImportanceState.text = getString(R.string.high_importance)
//                    tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_light_red))
//                }
//                else -> {
//                    tvImportanceState.text = getString(R.string.hint_no)
//                    tvImportanceState.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_gray))
//                }
//            }
//            true
//        }

        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        editText.addTextChangedListener {
            viewModel.resetErrorInputText()
        }
    }

    private fun hideBottomSheetMenus() {
        bottomSheetBehaviorImportance.state = BottomSheetBehavior.STATE_COLLAPSED
        binding.bottomSheetBackGround.visibility = View.GONE
    }


}
