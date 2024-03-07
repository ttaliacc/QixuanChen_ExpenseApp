package com.bignerdranch.android.expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.bignerdranch.android.expenseapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentHomeBinding? = null
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseModelCreater((activity?.application as ExpenseCreater).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ExpenseAdapter { expense ->
            findNavController().navigate(HomeFragmentDirections.actionExpensesListFragmentToAddEditExpenseFragment(expense.id))
        }
        binding.expensesRecyclerView.adapter = adapter
        binding.expensesRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
        }

        binding.btnFilter.setOnClickListener {
            val dateString = binding.etDateFilter.text.toString()
            if (dateString.isNotEmpty()) {
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                try {
                    val parsedDate: Date = dateFormat.parse(dateString)
                    val formattedTimestamp = parsedDate.time.toString()
                    viewModel.filterExpensesByDate(formattedTimestamp).observe(viewLifecycleOwner) { filteredExpenses ->
                        (binding.expensesRecyclerView.adapter as ExpenseAdapter).submitList(filteredExpenses)
                    }
                } catch (e: Exception) { }
            }
        }

        val categories = resources.getStringArray(R.array.total_categories)
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategoryFilter.adapter = spinnerAdapter

        binding.spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                if (selectedCategory == "All Categories") {
                    viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
                        (binding.expensesRecyclerView.adapter as ExpenseAdapter).submitList(expenses)
                    }
                } else {
                    viewModel.getExpensesByCategory(selectedCategory).observe(viewLifecycleOwner) { filteredExpenses ->
                        (binding.expensesRecyclerView.adapter as ExpenseAdapter).submitList(filteredExpenses)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnAddExpense.setOnClickListener {
            val action = HomeFragmentDirections.actionExpensesListFragmentToAddEditExpenseFragment(0) // 0 for new expense
            findNavController().navigate(action)
        }

        binding.btnCategorized.setOnClickListener {
            findNavController().navigate(R.id.action_expensesListFragment_to_expenseCategoryFragment)
        }

        binding.btnReset.setOnClickListener {
            binding.etDateFilter.text = null
            binding.spinnerCategoryFilter.setSelection(0)
            viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
                (binding.expensesRecyclerView.adapter as ExpenseAdapter).submitList(expenses)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}