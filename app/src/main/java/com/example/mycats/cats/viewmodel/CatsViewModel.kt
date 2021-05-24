package com.example.mycats.cats.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mycats.cats.repository.CatsRepo

class CatsViewModel(repo: CatsRepo) : ViewModel() {
    val cat = repo.cat
}