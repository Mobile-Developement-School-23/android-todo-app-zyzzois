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
import com.example.domain.entity.remote.Result
import com.example.presentation.R
import com.example.presentation.databinding.FragmentListBinding
import com.example.presentation.di.PresentationComponentProvider
import com.example.presentation.ui.core.factories.ViewModelFactory
import com.example.presentation.ui.screens.auth.AuthActivity
import com.example.presentation.ui.screens.main.recycler.SwipeTodoItemCallback
import com.example.presentation.ui.screens.main.recycler.ToDoListAdapter
import com.example.presentation.ui.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.presentation.ui.util.Constants.COMPLETED
import com.example.presentation.ui.util.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as PresentationComponentProvider).providePresentationComponent().create()
    }

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
        hideListAndShowShimmer()
        observeViewModel()
        setupRecyclerView()
        setupButtons()
        setupSwipeToRefresh()
    }

    private fun setupSwipeToRefresh() {
        binding.swipeToRefresh.setOnRefreshListener {
            hideListAndShowShimmer()
            viewModel.loadData()
            binding.swipeToRefresh.isRefreshing = false
            hideShimmerAndShowList()
            showToast(resources.getString(R.string.successfully_synchronized))
        }
    }

    private fun hideShimmerAndShowList() = with(binding) {
        shimmerViewContainer.visibility = View.GONE
        shimmerViewContainer.stopShimmer()
        listContainer.visibility = View.VISIBLE
    }

    private fun hideListAndShowShimmer() = with(binding) {
        listContainer.visibility = View.GONE
        shimmerViewContainer.visibility = View.VISIBLE
        shimmerViewContainer.startShimmer()
    }

    private fun observeViewModel() = with(viewModel) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    toDoList().collect {
                        listAdapter.submitList(it)
                        hideShimmerAndShowList()
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
                Result.AUTH_ERROR -> showNotAuthorizedDialog()
                Result.INTERNET_CONNECTION_ERROR -> showNetWorkConnectionDialog()
                else -> {}
            }
        }
        shouldShowNetworkError.observe(viewLifecycleOwner) { disconnected ->
            if (disconnected)
                showNetWorkConnectionDialog()
        }
    }

    private fun setupItemClickListener() {
        listAdapter.onItemClickListener = {
//            findNavController().navigate(
//                ListFragmentDirections.actionListFragmentToDetailFragment()
//                    .setTodoItemId(it.id)
//                    .setMode(MODE_EDIT)
//            )
        }
    }

    private fun setupRecyclerView() {
        with(binding.rcView) {
            listAdapter = ToDoListAdapter()
            adapter = listAdapter
        }
        setupItemClickListener()
        val swipeCallback = SwipeTodoItemCallback(
            onSwipeLeft = { position ->
                binding.swipeToRefresh.isRefreshing = false
                val item = listAdapter.getItem(position)
                viewModel.deleteToDoItem(item.id)
            },
            onSwipeRight = { position ->
                binding.swipeToRefresh.isRefreshing = false
               val item = listAdapter.getItem(position)
                viewModel.editToDoItem(item)
            },
            applicationContext = requireActivity().baseContext
        )
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.rcView)
    }

    private fun setupButtons() = with(binding) {
        buttonAuth.setOnClickListener {
            startActivity(AuthActivity.newIntentOpenAuthActivity(requireContext()))
        }
        buttonAddItem.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment())
        }
        buttonVisibility.setOnClickListener {
            if (isIcon1Visible)
                buttonVisibility.setImageResource(R.drawable.ic_invisible)
            else
                buttonVisibility.setImageResource(R.drawable.ic_eye)
            isIcon1Visible = !isIcon1Visible
        }
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

    private fun showNotAuthorizedDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.warning))
            .setMessage(resources.getString(R.string.warning_content))
            .setPositiveButton(resources.getString(R.string.authorize)) { _, _ ->
                startActivity(AuthActivity.newIntentOpenAuthActivity(requireContext()))
            }
            .setNeutralButton(resources.getString(R.string.ignore)) { _, _ -> }
            .show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}