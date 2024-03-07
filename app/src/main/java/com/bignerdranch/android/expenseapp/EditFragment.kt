package com.bignerdranch.android.expenseapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.bignerdranch.android.expenseapp.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val args: EditFragmentArgs by navArgs()
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseModelCreater((activity?.application as ExpenseCreater).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = resources.getStringArray(R.array.categories)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        val id = args.expenseId
        if (id > 0) {
            viewModel.getExpenseById(id).observe(viewLifecycleOwner) { expense ->
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                binding.editTextDate.setText(dateFormat.format(expense.date))
                binding.editTextAmount.setText(expense.amount.toString())
                val category = resources.getStringArray(R.array.categories)
                binding.categorySpinner.setSelection(category.indexOf(expense.category))
            }
        }
        binding.saveExpenseButton.setOnClickListener {
            if (id > 0) {
                val (amount, date, category) = getInput()
                if (amount != null && date != null) {
                    val expense = Expense(id = id, date = date, amount = amount, category = category)
                    viewModel.update(expense)
                    findNavController().navigateUp()
                }
            } else {
                val (amount, date, category) = getInput()
                if (amount != null && date != null) {
                    val expense = Expense(date = date, amount = amount, category = category)
                    viewModel.insert(expense)
                    findNavController().navigateUp()
                }
            }
        }
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getInput(): Triple<Double?, Date?, String> {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
        val dateString = binding.editTextDate.text.toString()
        val category = binding.categorySpinner.selectedItem.toString()

        if (amount == null) {
            return Triple(null, null, "")
        }
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val date = try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
        if (date == null) {
            return Triple(null, null, "")
        }
        return Triple(amount, date, category)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}