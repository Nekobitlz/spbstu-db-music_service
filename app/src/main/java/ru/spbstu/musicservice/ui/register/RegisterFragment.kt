package ru.spbstu.musicservice.ui.register

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.spbstu.musicservice.R
import ru.spbstu.musicservice.data.Gender
import ru.spbstu.musicservice.data.User
import ru.spbstu.musicservice.data.UserType
import ru.spbstu.musicservice.databinding.FragmentRegisterBinding
import ru.spbstu.musicservice.ui.State

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding()
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as? AppCompatActivity
        activity?.supportActionBar?.let {
            it.setDisplayShowCustomEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = resources.getString(R.string.registration)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_register, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.register -> viewModel.onRegisterClick(User(
                "",
                binding.etFirstName.text.toString(),
                binding.etSecondName.text.toString(),
                binding.etPhoneNumber.text.toString(),
                binding.etBirthday.text.toString(),
                0,
                binding.etEmail.text.toString(),
                if (binding.spinnerGender.editableText.toString() == getString(R.string.female))
                    Gender("2", getString(R.string.female))
                else Gender("1", getString(R.string.male)),
                UserType("1", "Пользователь"),
            ), binding.etPassword.text.toString())
            android.R.id.home -> activity?.onBackPressed()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val items = arrayOf(getString(R.string.male), getString(R.string.female))
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            items
        )
        binding.spinnerGender.setAdapter(adapter)
        viewModel.userState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Success -> {
                    Toast.makeText(
                        context,
                        getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.onBackPressed()
                }
                is State.Error -> Toast.makeText(context,
                    getString(R.string.register_error),
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}