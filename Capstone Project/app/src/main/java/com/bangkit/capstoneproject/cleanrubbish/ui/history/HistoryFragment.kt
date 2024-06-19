package com.bangkit.capstoneproject.cleanrubbish.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.capstoneproject.cleanrubbish.R
import com.bangkit.capstoneproject.cleanrubbish.adapter.HistoryAdapter
import com.bangkit.capstoneproject.cleanrubbish.data.local.db.History
import com.bangkit.capstoneproject.cleanrubbish.data.local.repo.HistoryRepository
import com.bangkit.capstoneproject.cleanrubbish.databinding.FragmentHistoryBinding
import com.bangkit.capstoneproject.cleanrubbish.helper.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private val adapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val historyRepository = HistoryRepository(requireActivity().application)
        val factory = ViewModelFactory.getInstance(historyRepository)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        setRecyclerView()

        viewModel.history.observe(viewLifecycleOwner) { history ->
            if (history.isNullOrEmpty()) {
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.tvEmptyMessage.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE

                val items = arrayListOf<History>()
                history.map { historyResult ->
                    val item = History(image = historyResult.image, label = historyResult.label)
                    items.add(item)
                }
                adapter.submitList(items)
                binding.rvHistory.adapter = adapter
            }
        }

        return root
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}