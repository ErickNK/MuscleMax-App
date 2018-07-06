package com.flycode.musclemax_app.ui.main.tags

import android.os.Bundle
import com.flycode.musclemax_app.ui.base.BaseContract

interface TagsContract {
    interface TagsFragment : BaseContract.View{

    }
    interface TagsPresenter<V : TagsFragment> : BaseContract.Presenter<V>{

    }
}