package com.bangkit.capstoneproject.cleanrubbish.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.bangkit.capstoneproject.cleanrubbish.R
import com.bangkit.capstoneproject.cleanrubbish.data.local.repo.HistoryRepository

import com.bangkit.capstoneproject.cleanrubbish.databinding.FragmentProfileBinding
import com.bangkit.capstoneproject.cleanrubbish.helper.ViewModelFactory
import com.bangkit.capstoneproject.cleanrubbish.ui.landing.WelcomeActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val historyRepository = HistoryRepository(requireActivity().application)
        val factory = ViewModelFactory.getInstance(historyRepository)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        setupName()
        binding.btnLogout.setOnClickListener { logout() }
        return root
    }

    private fun setupName() {
        val sharedPreferences = requireContext().getSharedPreferences(getString(R.string.user_prefs), Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(getString(R.string.user_name), null)
        if (!userName.isNullOrEmpty()) {
            binding.tvProfile.text = getString(R.string.profile_name, userName)
        }
    }
    private fun logout() {
        AlertDialog.Builder(requireActivity()).apply {
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.logout_validation))
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                val sharedPreferences = requireContext().getSharedPreferences(getString(R.string.user_prefs), Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    clear()
                    apply()
                }

                viewModel.deleteHistory()

                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                activity?.finish()
            }
            setNegativeButton(getString(R.string.no)){ _, _ ->
                return@setNegativeButton
            }
            create()
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}