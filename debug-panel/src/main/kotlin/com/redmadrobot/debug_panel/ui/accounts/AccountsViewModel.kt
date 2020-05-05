package com.redmadrobot.debug_panel.ui.accounts

import androidx.lifecycle.MutableLiveData
import com.redmadrobot.debug_panel.data.accounts.AccountRepository
import com.redmadrobot.debug_panel.data.accounts.AccountsProvider
import com.redmadrobot.debug_panel.data.storage.entity.DebugAccount
import com.redmadrobot.debug_panel.extension.observeOnMain
import com.redmadrobot.debug_panel.extension.zipList
import com.redmadrobot.debug_panel.ui.accounts.item.AccountItem
import com.redmadrobot.debug_panel.ui.base.BaseViewModel
import com.xwray.groupie.kotlinandroidextensions.Item
import io.reactivex.rxkotlin.subscribeBy

class AccountsViewModel(
    private val accountsRepository: AccountRepository,
    private val localAccountProvider: AccountsProvider,
    private val preInstalledAccountProvider: AccountsProvider
) : BaseViewModel() {

    val accounts = MutableLiveData<List<Item>>()

    fun loadAccounts() {
        localAccountProvider.getAccounts()
            .zipList(preInstalledAccountProvider.getAccounts())
            .observeOnMain()
            .map { it.map(::AccountItem) }
            .subscribeBy(onSuccess = { accounts.value = it })
            .autoDispose()
    }

    fun addAccount(account: DebugAccount) {
        accountsRepository
            .addCredential(account)
            .observeOnMain()
            .subscribeBy(
                onComplete = {
                    val newAccountList = (accounts.value as MutableList<Item>).apply {
                        add(AccountItem(account))
                    }
                    accounts.value = newAccountList
                }
            )
            .autoDispose()
    }

    fun removeAccount(position: Int) {
        val accountItems = accounts.value as List<Item>
        val item = accountItems[position] as AccountItem
        val data = item.account

        accountsRepository
            .removeCredential(data)
            .observeOnMain()
            .subscribeBy(
                onComplete = {
                    val newAccountList = accountItems.toMutableList().apply {
                        removeAt(position)
                    }
                    accounts.value = newAccountList
                }
            )
            .autoDispose()
    }
}
