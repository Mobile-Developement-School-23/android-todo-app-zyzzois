package com.example.presentation.ui.screens.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.remote.Result
import com.example.presentation.R
import com.example.presentation.databinding.FragmentListBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.ui.core.factories.ViewModelFactory
import com.example.presentation.ui.core.network.ConnectionListener
import com.example.presentation.ui.screens.main.recycler.SwipeTodoItemCallback
import com.example.presentation.ui.screens.main.recycler.ToDoListAdapter
import com.example.presentation.ui.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.presentation.ui.util.Constants.COMPLETED
import com.example.presentation.ui.util.Constants.MODE_EDIT
import com.example.presentation.ui.util.showToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as PresentationComponentProvider).providePresentationComponent().create()
    }

    private val connectionListener by lazy { ConnectionListener(requireActivity().application) }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)[ListViewModel::class.java]
    }

    private lateinit var listAdapter: ToDoListAdapter

    private var isIcon1Visible = true

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding ?: throw RuntimeException(BINDING_NULL_EXCEPTION_MESSAGE)

    private lateinit var bottomActions: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomRename: BottomSheetBehavior<LinearLayout>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
        setupBottomSheet()
        setupButtons()
        setupSwipeListener(binding.rcView)
        //setupCheckBoxClickListener()
    }

    private fun observeViewModel() = with(viewModel) {
        //updateList()
        toDoList.observe(viewLifecycleOwner) {
            updateCompletedNumber()
            listAdapter.submitList(it)
        }
        requestResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                Result.INTERNET_CONNECTION_ERROR -> showNetWorkConnectionDialog()
                else -> {}
            }
        }

        connectionListener.observe(viewLifecycleOwner) { connected ->
            if (connected) {
                viewModel.loadData()
            }
            else showSnackBar("вы не подключены к сети")
        }
    }

    private fun setupBottomSheet()  {
        bottomActions = BottomSheetBehavior.from(binding.bottomMenuActions.bottomActions)
        bottomRename = BottomSheetBehavior.from(binding.bottomMenuRename.bottomMenu)
        bottomActions.peekHeight = 0
        bottomRename.peekHeight = 0
        bottomActions.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomRename.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupItemLongClickListener() = with(binding) {
        listAdapter.onItemLongClickListener = {
            buttonAddItem.visibility = View.GONE
            val selectedItem = it

            bottomActions.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBackGround.visibility = View.VISIBLE

            bottomSheetBackGround.setOnClickListener {
                hideBottomMenus()
            }
            bottomMenuRename.buttonDeleteOnBottomMenu.setOnClickListener {
                hideBottomMenus()
            }
            bottomMenuActions.buttonDelete.setOnClickListener {
                hideBottomMenus()
            }

            bottomMenuActions.buttonRename.setOnClickListener {
                bottomSheetBackGround.visibility = View.GONE
                bottomActions.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomRename.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBackGround.visibility = View.VISIBLE
                bottomMenuRename.inputFileName.setText(selectedItem.text)
                if (bottomMenuRename.inputFileName.text != null) {
                    bottomMenuRename.buttonSaveOnBottomMenu.setOnClickListener {
                        val newName = bottomMenuRename.inputFileName.text.toString()
                        viewModel.renameToDoItem(selectedItem, newName)
                        hideBottomMenus()
                    }
                }
            }
            bottomMenuActions.buttonDelete.setOnClickListener {
                hideBottomMenus()
            }

        }
    }

    private fun setupItemClickListener() {
        listAdapter.onItemClickListener = {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment()
                    .setTodoItemId(it.id)
                    .setMode(MODE_EDIT)
            )
        }
    }

    private fun setupRecyclerView() {
        with(binding.rcView) {
            listAdapter = ToDoListAdapter()
            adapter = listAdapter
        }
        setupItemClickListener()
        setupItemLongClickListener()
    }

    private fun hideBottomMenus() = with(binding) {
        buttonAddItem.visibility = View.VISIBLE
        bottomActions.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomRename.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBackGround.visibility = View.GONE
    }

    private fun setupButtons() = with(binding) {

        binding.buttonSync.setOnClickListener {
            viewModel.updateList()
            viewModel.loadData()
            if (viewModel.requestResult.value == Result.SUCCESS) {
                showToast(requireContext().getString(R.string.successfully_synchronized))
            }
        }

        buttonAuth.setOnClickListener {
            startActivity(com.example.presentation.ui.screens.auth.AuthActivity.newIntentOpenAuthActivity(requireContext()))
        }

        buttonAddItem.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment()
            )
        }
        viewModel.completedNumber.observe(viewLifecycleOwner) {
            tvCompleted.text = String.format(COMPLETED, it)
        }

        buttonVisibility.setOnClickListener {
            if (isIcon1Visible)
                buttonVisibility.setImageResource(R.drawable.ic_invisible)
            else
                buttonVisibility.setImageResource(R.drawable.ic_eye)
            isIcon1Visible = !isIcon1Visible
        }
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val swipeCallback = SwipeTodoItemCallback(
            onSwipeLeft = { position ->
                val item = listAdapter.currentList[position]
                viewModel.deleteToDoItem(item.id)
                viewModel.updateList()
            },
            onSwipeRight = { position ->
                val item = listAdapter.currentList[position]
                viewModel.editToDoItem(item, completed = !item.completed)
                viewModel.updateList()
            },
            applicationContext = requireActivity().baseContext
        )

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun showNetWorkConnectionDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.connection_error_title))
            .setMessage(resources.getString(R.string.connection_error_message))
            .setPositiveButton(resources.getString(R.string.OkText)) { _, _ ->
                showSnackBar(resources.getString(R.string.show_cashed_todos_text))
            }
            .show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
        viewModel.updateList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}