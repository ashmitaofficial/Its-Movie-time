package e.ashmita.netflix.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import e.ashmita.netflix.R
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import e.ashmita.netflix.home.HomeActivity
import e.ashmita.netflix.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var userId: String
    lateinit var userReference: DatabaseReference

    lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSigninBinding.inflate(inflater, container, false)
        activity?.window?.decorView?.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.backToLogin.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.container, LoginFragment::class.java, null)
                .commit()
        }

        binding.regsiterBtn.setOnClickListener {
            registerUser()
        }


        return binding.root
    }

    private fun registerUser() {
        val userEmail = binding.emailTxt.text.toString()
        val password = binding.passFieldSignin.text.toString().trim()

        if (userEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
        } else {
            firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //here we get the id of logged in user
                        userId = firebaseAuth.currentUser!!.uid

                        userReference =
                            FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                        val userMap = HashMap<String, Any>()
                        userMap["uid"] = userId
                        userMap["uEmail"] = userEmail
                        userMap["uPass"] = password

                        userReference.updateChildren(userMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
//                                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                activity?.finish()
                            } else {
                                Toast.makeText(activity, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
        }
    }
}
