package ru.spbstu.musicservice.ui.payments

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.adapter.BaseAdapterItem
import ru.spbstu.commons.layoutInflater
import ru.spbstu.commons.setTextOrHide
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Payment
import ru.spbstu.musicservice.databinding.ItemPaymentBinding

class PaymentItem(
    private val item: Payment,
) : BaseAdapterItem<PaymentViewHolder>() {
    override val viewType: Int
        get() = R.id.view_type_payment

    override fun createViewHolder(parent: ViewGroup): PaymentViewHolder {
        return PaymentViewHolder(
            ItemPaymentBinding.inflate(parent.layoutInflater, parent, false)
        )
    }

    override fun bind(holder: PaymentViewHolder) {
        holder.bind(item)
    }
}


class PaymentViewHolder(
    private val binding: ItemPaymentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Payment) {
        val resources = itemView.resources
        binding.tvPaymentDate.setTextOrHide(resources.getString(R.string.payment_date, item.date))
        binding.tvAmount.setTextOrHide(resources.getString(R.string.payment_amount, item.amount))
    }
}