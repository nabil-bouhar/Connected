package com.example.connected.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connected.models.User

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel(),
    HomeRepository.UsersCallback {

    private val usersCallback = this

    var users: MutableLiveData<MutableList<User>> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    fun getUsers() {
        homeRepository.loadUsersListFromFireStore(usersCallback)
    }

    override fun onReceivedUsersList(users: MutableList<User>) {
        this.users.value = users
        loading.value = false
    }

    override fun onErrorReceivingUsersList(error: String) {
        this.error.value = error
        loading.value = false
    }

    /*
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                val filteredUsersList = if (charString.isEmpty()) users.value
                else {
                    val filteredList = ArrayList<Device>()
                    val listToFilter: MutableList<Device> =
                        if (devicesListBeforeFilter.isNotEmpty()) devicesListBeforeFilter else devicesList.value!!
                    listToFilter.filter {
                        (it.productType?.equals(constraint!!) ?: false)
                    }
                        .forEach { filteredList.add(it) }
                    filteredList
                }
                return FilterResults().apply { values = filteredDeviceList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDeviceList = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<Device>

                if (devicesListBeforeFilter.isEmpty()) {
                    devicesListBeforeFilter = devicesList.value!!
                }
                devicesList.value = filteredDeviceList
            }
        }
    }
    
     */
}