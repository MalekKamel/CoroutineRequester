package com.coroutinerequester.app.ui.restaurants

import android.view.ViewGroup
import com.coroutinerequester.app.R
import com.coroutinerequester.app.data.model.Restaurant
import com.coroutinerequester.app.presentation.view.BaseRecyclerAdapter
import com.coroutinerequester.app.presentation.view.BaseViewHolder
import com.coroutinerequester.app.util.picasso.PicassoUtil
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(list: MutableList<Restaurant>) : BaseRecyclerAdapter<Restaurant, RestaurantsAdapter.Vh>(list) {

    override fun getViewHolder(viewGroup: ViewGroup, viewType: Int): Vh {
        return Vh(viewGroup)
    }

    inner class Vh internal constructor(viewGroup: ViewGroup)
        : BaseViewHolder<Restaurant>(viewGroup, R.layout.item_restaurant) {


        override fun bindView(item: Restaurant) {
            itemView.tvName.text = item.name
            itemView.tvCity.text = item.city
            PicassoUtil.load(
                    iv =itemView.ivPoster,
                    url = item.poster
            )
        }

    }
}
