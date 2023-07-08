package com.example.presentation.ui.screens.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.remote.Result
import com.example.presentation.R
import com.example.presentation.databinding.FragmentListBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.ui.core.factories.ViewModelFactory
import com.example.presentation.ui.core.network.ConnectionListener
import com.example.presentation.ui.screens.auth.AuthActivity
import com.example.presentation.ui.screens.main.recycler.SwipeTodoItemCallback
import com.example.presentation.ui.screens.main.recycler.ToDoListAdapter
import com.example.presentation.ui.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.presentation.ui.util.Constants.COMPLETED
import com.example.presentation.ui.util.Constants.MODE_EDIT
import com.example.presentation.ui.util.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
        setupButtons()
        setupSwipeListener(binding.rcView)
    }

    private fun observeViewModel() = with(viewModel) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    toDoList().collect {
                        listAdapter.submitList(it)
                    }
                }
                launch {
                    viewModel.completedToDoNumber().collect {
                        binding.tvCompleted.text = String.format(COMPLETED, it)
                    }
                }
            }
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
            } else {
                showSnackBar("вы не подключены к сети")
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
    }

    private fun setupButtons() = with(binding) {
        buttonSync.setOnClickListener {
            viewModel.loadData()
            if (viewModel.requestResult.value == Result.SUCCESS) {
                showToast(requireContext().getString(R.string.successfully_synchronized))
            }
        }

        buttonAuth.setOnClickListener {
            startActivity(AuthActivity.newIntentOpenAuthActivity(requireContext()))
        }

        buttonAddItem.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
        }

        buttonVisibility.setOnClickListener {
            if (isIcon1Visible) {
                buttonVisibility.setImageResource(R.drawable.ic_invisible)
            } else
                buttonVisibility.setImageResource(R.drawable.ic_eye)
            isIcon1Visible = !isIcon1Visible
        }
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val swipeCallback = SwipeTodoItemCallback(
            onSwipeLeft = { position ->
                val item = listAdapter.currentList[position]
                viewModel.deleteToDoItem(item.id)
            },
            onSwipeRight = { position ->
                val item = listAdapter.currentList[position]
                viewModel.editToDoItem(item, completed = !item.completed)
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
}
