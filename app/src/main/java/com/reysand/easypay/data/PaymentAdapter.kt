package com.reysand.easypay.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reysand.easypay.databinding.ListItemPaymentBinding

class PaymentAdapter : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private var paymentList: List<Payment> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPaymentBinding.inflate(inflater, parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = paymentList[position]
        holder.bind(payment)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    fun updatePayments(newPayments: List<Payment>) {
        paymentList = newPayments
        notifyDataSetChanged()
    }

    class PaymentViewHolder(private val binding: ListItemPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.paymentId.text = payment.id.toString()
            binding.paymentTitle.text = payment.title
            binding.paymentAmount.text = if (payment.amount.isNullOrEmpty()) "0.0" else payment.amount
            binding.paymentCreated.text = java.time.format.DateTimeFormatter.ISO_INSTANT
                .format(java.time.Instant.ofEpochSecond(payment.created))
        }
    }
}