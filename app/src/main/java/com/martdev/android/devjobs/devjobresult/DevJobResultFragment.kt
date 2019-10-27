package com.martdev.android.devjobs.devjobresult

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.martdev.android.devjobs.DevJobFactory
import com.martdev.android.devjobs.Injectors
import com.martdev.android.devjobs.R
import com.martdev.android.devjobs.data.Result
import com.martdev.android.devjobs.databinding.DevjobsResultFragmentBinding
import com.martdev.android.devjobs.util.ConnectivityUtil
import kotlinx.android.synthetic.main.devjobs_result_fragment.*

class DevJobResultFragment : Fragment() {

    private val arg by navArgs<DevJobResultFragmentArgs>()

    private val viewModel: DevJobResultVM by viewModels {
        DevJobFactory(keyword = arg.keyword, repository = Injectors.provideDevJobRepository(activity!!.application))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<DevjobsResultFragmentBinding>(inflater,
                R.layout.devjobs_result_fragment, container, false)

        binding.lifecycleOwner = this

        binding.devJobVM = viewModel

        binding.devjobRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        binding.devjobRecyclerView.adapter = DevJobAdapter(DevJobAdapter.OnClickListener {
            viewModel.showJobDetails(it)
        })

        viewModel.devJobs.observe(this, Observer {
            if (it.isNullOrEmpty()) no_data_message.visibility = View.VISIBLE
            else no_data_message.visibility = View.GONE
        })

        viewModel.navigateToJobDetail.observe(this, Observer {
            if (it != null) {
                this.findNavController().navigate(DevJobResultFragmentDirections.actionDevJobResultFragmentToDevJobDetail(it))
                viewModel.jobDetailShown()
            }
        })

        viewModel.networkState.observe(this, Observer {
            if (it.status == Result.Status.ERROR) {
                Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        val isConnected = ConnectivityUtil.isConnected(context!!)
        if (isConnected) {
            viewModel.connectivityAvailable = isConnected
        } else {
            Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.keyword_search, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        query(searchView)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun query(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    viewModel.searchForJob(query)
                    searchView.onActionViewCollapsed()
                    activity?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                    true
                } else false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
}