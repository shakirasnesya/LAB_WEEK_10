package com.example.map_lab_week_10

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.map_lab_week_10.viewmodels.TotalViewModel

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViewModel()
    }

    private fun prepareViewModel() {
        // requireActivity() penting agar Fragment memakai ViewModel milik Activity (Shared ViewModel)
        val viewModel = ViewModelProvider(requireActivity())[TotalViewModel::class.java]

        // Observe perubahan data
        viewModel.total.observe(viewLifecycleOwner) { total ->
            updateText(total)
        }
    }

    private fun updateText(total: Int) {
        view?.findViewById<TextView>(R.id.text_total)?.text = getString(R.string.text_total, total)
    }
}