package com.reysand.easypay.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.reysand.easypay.data.PaymentAdapter
import com.reysand.easypay.databinding.FragmentDashboardBinding
import com.reysand.easypay.ui.EasyPayViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val easyPayViewModel: EasyPayViewModel by activityViewModels { EasyPayViewModel.provideFactory() }

    // Views
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentAdapter: PaymentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.recyclerView
        paymentAdapter = PaymentAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = paymentAdapter

        easyPayViewModel.currentLogin.value?.values?.let { dashboardViewModel.fetchPayments(easyPayViewModel, it.first()) }

        dashboardViewModel.paymentListResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is PaymentListResult.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Fetch success: ${result.paymentList?.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Update recyclerview
                    Log.d("DashboardFragment", "${result.paymentList}")
                    result.paymentList?.let { paymentAdapter.updatePayments(it) }
                }

                is PaymentListResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Fetch failed: ${result.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }

        easyPayViewModel.currentLogin.observe(viewLifecycleOwner) { login ->
            if (login == null) {
                paymentAdapter.updatePayments(emptyList())
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}