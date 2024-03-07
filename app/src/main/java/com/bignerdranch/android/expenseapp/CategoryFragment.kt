package com.bignerdranch.android.expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bignerdranch.android.expenseapp.databinding.FragmentCagetoryBinding

class CategoryFragment : Fragment() {

    private var _binding: FragmentCagetoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseModelCreater((activity?.application as ExpenseCreater).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCagetoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    // update list of category groupings for expenses
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val CategoryAdapter = CategoryAdapter()
        binding.categorizedExpensesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.categorizedExpensesRecyclerView.adapter = CategoryAdapter
        viewModel.expensesGroupedByCategory.observe(viewLifecycleOwner) { categorizedExpenses ->
            CategoryAdapter.submitList(categorizedExpenses)
        }
        val btnBack: Button = view.findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}