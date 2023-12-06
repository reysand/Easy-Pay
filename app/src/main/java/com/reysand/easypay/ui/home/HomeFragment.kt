package com.reysand.easypay.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.reysand.easypay.R
import com.reysand.easypay.databinding.FragmentHomeBinding
import com.reysand.easypay.ui.EasyPayViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val easyPayViewModel: EasyPayViewModel by activityViewModels { EasyPayViewModel.provideFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.loginTextView
        textView.text = getString(R.string.logged_in_as, "null")
        easyPayViewModel.currentLogin.observe(viewLifecycleOwner) { login ->
            textView.text = getString(R.string.logged_in_as, login?.keys?.firstOrNull() ?: "null")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}