package e.ashmita.netflix.authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import e.ashmita.netflix.home.HomeActivity
import e.ashmita.netflix.R
import e.ashmita.netflix.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        activity?.window?.decorView?.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        firebaseAuth = FirebaseAuth.getInstance()


        binding.signInLink.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, SigninFragment::class.java, null)
                .commit()
        }

        binding.loginBtn.setOnClickListener {
            loginUser()
        }


        return binding.root
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------//

    private fun loginUser() {
        val email = binding.emailField.text.toString().trim()
        val password = binding.passField.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
        } else {
            //signInWithEmailAndPassword this function sign in the user if it already has account
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        activity?.finish()
                    } else {
                        Toast.makeText(
                            context,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }

//-------------------------------------------------------------------------------------------------------------------------------------------------------//

}