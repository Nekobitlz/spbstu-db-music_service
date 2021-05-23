package ru.spbstu.musicservice.ui.user_info

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.databinding.FragmentUserInfoBinding
import ru.spbstu.musicservice.ui.feed.MusicFeedFragment.Companion.PARAM_USER

class UserInfoFragment : Fragment(R.layout.fragment_user_info) {

    private val binding: FragmentUserInfoBinding by viewBinding()

    override fun onStart() {
        super.onStart()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getSerializable(PARAM_USER) as User

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.etName.setText(user.firstName + " " + user.secondName, TextView.BufferType.EDITABLE)
        binding.tvUserType.text = user.userType.type

        binding.etPhoneNumber.setText(user.phoneNumber, TextView.BufferType.EDITABLE)
        binding.etEmail.setText(user.email, TextView.BufferType.EDITABLE)
        binding.etBirthday.setText(user.birthday, TextView.BufferType.EDITABLE)
        binding.etGender.setText(user.gender.gender, TextView.BufferType.EDITABLE)
        binding.etCountry.setText(user.country.name, TextView.BufferType.EDITABLE)

        binding.itemSubscription.tvStartDate.text = user.subscription.startDate
        binding.itemSubscription.tvEndDate.text = user.subscription.endDate
        binding.itemSubscription.btnGoToSubscription.setOnClickListener {
            Toast.makeText(context, "SUBSCRIPTION", Toast.LENGTH_SHORT).show()
        }
    }
}