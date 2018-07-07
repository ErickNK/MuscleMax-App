package com.flycode.musclemax_app.ui.main.coach.coachOverview


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import com.flycode.musclemax_app.R
import com.flycode.musclemax_app.data.models.User
import com.flycode.musclemax_app.databinding.CoachOverviewBinding
import com.flycode.musclemax_app.ui.base.BaseFragment
import com.flycode.musclemax_app.ui.flexibleItems.CoachesHeaderItem
import com.flycode.musclemax_app.ui.flexibleItems.CoachesListItem
import com.flycode.musclemax_app.ui.main.MainActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager
import eu.davidea.flexibleadapter.utils.Log
import kotlinx.android.synthetic.main.fragment_coach_overview.*
import javax.inject.Inject

class CoachOverviewFragment 
    : BaseFragment<CoachOverviewFragment, CoachOverviewPresenter, CoachOverviewViewModel>(),
        CoachOverviewContract.CoachOverviewFragment,
        CoachesHeaderItem.CoachesHeaderItemListener,
        CoachesListItem.CoachListItemListener {

    @Inject override lateinit var viewModel: CoachOverviewViewModel
    @Inject lateinit var mainListAdapter: FlexibleAdapter<CoachesHeaderItem>
    @Inject lateinit var mainActivity: MainActivity
    lateinit var coachOverviewBinding: CoachOverviewBinding
    private var input_finish_delay: Long = 1000 // 1 seconds after user stops typing
    private var input_finish_handler = Handler()
    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > viewModel.lastTextEdit + input_finish_delay - 500) {
            if (mainListAdapter.hasNewFilter(coachOverviewBinding.toolbar.findViewById<EditText>(R.id.ed_search).text.toString())){
                mainListAdapter.setFilter(coachOverviewBinding.toolbar.findViewById<EditText>(R.id.ed_search).text.toString())
                mainListAdapter.filterItems()
                coachOverviewBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger)
                        .setImageResource(R.drawable.ic_close_black_24dp)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        coachOverviewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_coach_overview,container,false)
        coachOverviewBinding.viewModel = viewModel
        coachOverviewBinding.setLifecycleOwner(this)

        return coachOverviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coachOverviewBinding.toolbar.findViewById<EditText>(R.id.ed_search).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                //You need to remove this to run only once
                input_finish_handler.removeCallbacks(input_finish_checker)

            }

            override fun afterTextChanged(s: Editable) {
                //avoid triggering event when text is empty
                if (s.isNotEmpty()) {
                    viewModel.lastTextEdit = System.currentTimeMillis()
                    input_finish_handler.postDelayed(input_finish_checker, input_finish_delay)
                }
            }
        })
        coachOverviewBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger).setOnClickListener {
            if (mainListAdapter.hasFilter()){
                mainListAdapter.notifyDataSetChanged()
                coachOverviewBinding.toolbar.findViewById<ImageButton>(R.id.btn_humberger)
                        .setImageResource(R.drawable.ic_humberger)
            }else mainActivity.toggleDrawer()
        }

        coachOverviewBinding.reloadBtn.setOnClickListener {
            presenter.fetchCoaches()
            viewModel.uiState.onError = false
        }

        setupMainRecyclerViews()
    }

    private fun setupMainRecyclerViews(){
        //ADAPTER
        FlexibleAdapter.enableLogs(Log.Level.DEBUG)
        FlexibleAdapter.useTag("MainAdapter")

        //HEADERS
        viewModel.headingsList =  ArrayList<CoachesHeaderItem>().apply {
            this.add(CoachesHeaderItem(1, "Favourites", 0).apply {
                this.listener = this@CoachOverviewFragment
                viewModel.favouritesHeader = this
            })
            this.add(CoachesHeaderItem(2, "All", 0).apply {
                this.listener = this@CoachOverviewFragment
                viewModel.allCoachesHeader = this
            })
        }
        mainListAdapter.addItems(0, viewModel.headingsList)

        val adapter = mainListAdapter
        // Non-exhaustive configuration that don't need RV instance
        adapter.addListener(this) //Only if you didn't use the Constructor
                .setStickyHeaders(true)
                .expandItemsAtStartUp() //Items must be pre-initialized with expanded=true
                .setAutoCollapseOnExpand(false) //Force closes all others expandable item before expanding a new one
                .setDisplayHeadersAtStartUp(true)
                .setAutoScrollOnExpand(true) //Needs a SmoothScrollXXXLayoutManager
                .setAnimationOnForwardScrolling(true) //Enable scrolling animation: entry + forward scrolling
                .setAnimationOnReverseScrolling(true) //Enable animation for reverse scrolling

        //LAYOUT MANAGER
        val layoutManager = SmoothScrollLinearLayoutManager(context)

        main_recycler_view.layoutManager = layoutManager
        main_recycler_view.setHasFixedSize(true)
        main_recycler_view.adapter = adapter

        mainListAdapter.expandAll()
    }

    override fun onExpand(position: Int) {
        mainListAdapter.expand(position)
    }

    override fun onCollapse(position: Int){
        mainListAdapter.collapse(position)
    }

    override fun onCoachClicked(coach: User) {
        presenter.onCoachClicked(coach)
    }

}
