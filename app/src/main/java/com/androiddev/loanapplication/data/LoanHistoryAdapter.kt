package com.androiddev.loanapplication.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androiddev.loanapplication.R
import com.androiddev.loanapplication.model.Loan

class LoanHistoryAdapter(val itemList: List<Loan>) :
    RecyclerView.Adapter<LoanHistoryAdapter.LoanHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return LoanHolder(v)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LoanHolder, position: Int) {
        holder.loanAmount.setText(itemList[position].amount.toString())
        holder.loanStatus.setText(itemList[position].status.toString())
        holder.loanDur.setText(itemList[position].duration.toString())
    }

    inner class LoanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var loanAmount: TextView
        var loanDur: TextView
        var loanStatus: TextView

        init {
            loanStatus = itemView.findViewById(R.id.tvLoanStatus)
            loanDur = itemView.findViewById(R.id.tvLoanDur)
            loanAmount = itemView.findViewById(R.id.tvLoanAmount)
        }

    }
}