package com.example.todo.ui.screens

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.app.ToDoApp
import com.example.todo.databinding.FragmentListBinding
import com.example.todo.ui.recycler.ToDoListAdapter
import com.example.todo.ui.viewmodels.ListViewModel
import com.example.todo.ui.viewmodels.ViewModelFactory
import com.example.todo.util.Constants.BINDING_NULL_EXCEPTION_MESSAGE
import com.example.todo.util.Constants.COMPLETED
import com.example.todo.util.Constants.MODE_EDIT
import com.google.android.material.bottomsheet.BottomSheetBehavior
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import javax.inject.Inject

class ListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as ToDoApp).component
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

    private lateinit var bottomSheetBehaviorActions: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorRename: BottomSheetBehavior<LinearLayout>

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
        viewModel.updateList()
        setupRecyclerView()
        viewModel.toDoList.observe(viewLifecycleOwner) {
            viewModel.updateCompletedNumber()
            listAdapter.submitList(it)
        }
        setupBottomSheet()
        setupClickListeners()
        setupSwipeListener(binding.rcView)
    }

    private fun setupBottomSheet() {
        bottomSheetBehaviorActions = BottomSheetBehavior.from(binding.bottomMenuActions.bottomActions)
        bottomSheetBehaviorRename = BottomSheetBehavior.from(binding.bottomMenuRename.bottomMenu)
        bottomSheetBehaviorActions.peekHeight = 0
        bottomSheetBehaviorRename.peekHeight = 0
        bottomSheetBehaviorActions.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorRename.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupItemLongClickListener() = with(binding) {
        listAdapter.onItemLongClickListener = {
            buttonAddItem.visibility = View.GONE
            val selectedItem = it

            bottomSheetBehaviorActions.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBackGround.visibility = View.VISIBLE

            bottomSheetBackGround.setOnClickListener {
                hideBottomSheetMenus()
            }
            bottomMenuRename.buttonDeleteOnBottomMenu.setOnClickListener {
                hideBottomSheetMenus()
            }
            bottomMenuActions.buttonDelete.setOnClickListener {
                hideBottomSheetMenus()
            }

            bottomMenuActions.buttonRename.setOnClickListener {
                bottomSheetBackGround.visibility = View.GONE
                bottomSheetBehaviorActions.state = BottomSheetBehavior.STATE_COLLAPSED
                bottomSheetBehaviorRename.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBackGround.visibility = View.VISIBLE
                bottomMenuRename.inputFileName.setText(selectedItem.text)
                if (bottomMenuRename.inputFileName.text != null) {
                    bottomMenuRename.buttonSaveOnBottomMenu.setOnClickListener {
                        val newName = bottomMenuRename.inputFileName.text.toString()
                        viewModel.renameToDoItem(selectedItem, newName)
                        hideBottomSheetMenus()
                    }
                }
            }
            bottomMenuActions.buttonDelete.setOnClickListener {
                hideBottomSheetMenus()
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

    private fun hideBottomSheetMenus() = with(binding) {
        buttonAddItem.visibility = View.VISIBLE
        bottomSheetBehaviorActions.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehaviorRename.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBackGround.visibility = View.GONE
    }

    private fun setupClickListeners() = with(binding) {
        buttonAddItem.setOnClickListener {
            findNavController().navigate(
                ListFragmentDirections.actionListFragmentToDetailFragment()
            )
        }
        viewModel.completedNumber.observe(viewLifecycleOwner) {
            tvCompleted.text = String.format(COMPLETED, it)
        }

        buttonVisibility.setOnClickListener {
            if (isIcon1Visible) {
                buttonVisibility.setImageResource(R.drawable.ic_invisible)
            } else {
                buttonVisibility.setImageResource(R.drawable.ic_eye)
            }
            isIcon1Visible = !isIcon1Visible
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                val item = listAdapter.currentList[position]
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteToDoItem(item.id)
                        viewModel.updateList()
                    }
                    ItemTouchHelper.RIGHT -> viewModel.editToDoItem(item, completed = !item.completed)
                }
                viewModel.updateList()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_checked)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_small_delete)
                    .setSwipeLeftLabelColor(ContextCompat.getColor(requireContext(), R.color.white))
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}