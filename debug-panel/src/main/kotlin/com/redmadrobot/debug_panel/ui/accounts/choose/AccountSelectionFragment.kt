package com.redmadrobot.debug_panel.ui.accounts.choose

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.redmadrobot.debug_panel.R
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.extension.observe
import com.redmadrobot.debug_panel.extension.obtainViewModel
import com.redmadrobot.debug_panel.internal.DebugPanel
import com.redmadrobot.debug_panel.ui.accounts.item.AccountItem
import com.redmadrobot.debug_panel.ui.base.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_account_select.*

class AccountSelectionFragment : BaseFragment(R.layout.fragment_account_select) {

    private val accountsViewModel by lazy {
        obtainViewModel {
            DebugPanel.getContainer().createAccountsViewModel()
        }
    }

    private val accountsAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(accountsViewModel.accounts, ::setAccountList)
        accountsViewModel.loadAccounts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }


    private fun setView() {
        account_select_recycler.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        accountsAdapter.setOnItemClickListener { item, _ ->
            val account = (item as? AccountItem)?.account
            account?.let(::selectAccount)
        }
    }

    private fun selectAccount(account: DebugAccount) {
        (targetFragment as? AccountDataResultListener)?.onAccountSelected(
            account.login,
            account.password
        )
        DebugPanel.authenticator?.authenticate(account)
    }

    private fun setAccountList(accounts: List<Item>) {
        accountsAdapter.update(accounts)
    }

    interface AccountDataResultListener {
        fun onAccountSelected(login: String, password: String)
    }
}
