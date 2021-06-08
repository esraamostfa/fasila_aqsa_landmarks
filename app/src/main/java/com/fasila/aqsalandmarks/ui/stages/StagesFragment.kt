package com.fasila.aqsalandmarks.ui.stages

import android.animation.ValueAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasila.aqsalandmarks.R
import com.fasila.aqsalandmarks.app.AqsaLandmarksApplication
import com.fasila.aqsalandmarks.databinding.FragmentStagesBinding
import com.fasila.aqsalandmarks.rootRef
import com.fasila.aqsalandmarks.ui.MainActivity
import com.fasila.aqsalandmarks.ui.profile.ChangePasswordDialogFragment
import com.fasila.aqsalandmarks.ui.profile.DeleteAccountDialogFragment
import com.fasila.aqsalandmarks.ui.stages.badges.BadgesRecyclerViewAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.profile_back_drop.*
import kotlinx.android.synthetic.main.profile_back_drop.view.*
import timber.log.Timber
import java.util.*


class StagesFragment : Fragment() {

    private lateinit var binding: FragmentStagesBinding
    private lateinit var viewModel: StagesViewModel
    private lateinit var adapter: StagesRecyclerViewAdapter
    private lateinit var layoutManager: GridLayoutManager

    private lateinit var badgesAdapter: BadgesRecyclerViewAdapter
    private lateinit var badgesLayoutManager: LinearLayoutManager
    val user = Firebase.auth.currentUser
    lateinit var name: String
    lateinit var email: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stages, container, false)

//        //set the tool bar
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        binding.toolBar.setNavigationOnClickListener(
            NavigationIconClickListener(
                requireActivity(),
                binding.stagesRecyclerView,
                AccelerateDecelerateInterpolator(),
                openIcon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.round_account_circle_white_36, null
                ),
                closeIcon = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.round_account_circle_green_36,
                    null
                )
            )
        )

        user?.let {
            name = user.displayName.toString()
            email = user.email.toString()

        }

        //initialize viewModel
        viewModel = ViewModelProvider(this).get(StagesViewModel::class.java)

        onClickStage()
        setupRecyclerView()
        //setupRecyclerViewSoon()
        setProfileBackDrop()
        AqsaLandmarksApplication.calculateStreak(false)
        setStreakColor()

        val streak = AqsaLandmarksApplication.sharedPref.getInt("streakCounter", 0)
        Timber.i(streak.toString())
        binding.streak.text = streak.toString()
        binding.streak.setOnClickListener {
            Toast.makeText(activity, "لقد تعلمت $streak أيام متواصلة", Toast.LENGTH_SHORT).show()
        }
        binding.hearts.text = AqsaLandmarksApplication.sharedPref.getString("hearts", "5")

        binding.aboutButton.setOnClickListener {
            findNavController().navigate(R.id.action_stagesFragment_to_aboutFragment)
        }

        setHasOptionsMenu(true)
        //setSupportActionBar()

        // TODO: Step 1.7 call create channel
        createChannel("aqsa_channel", "معالم الأقصى")

        return binding.root
    }


    //setup the recyclerView span
    private fun setSpanSize(layoutManager: GridLayoutManager) {
        val singleSpanPos =
            listOf(0, 15, 16, 21, 22, 29, 30, 46, 47, 48, 61, 62, 65, 66, 75, 76, 91, 92, 96, 97, 101)
        val doubleSpanPos =
            listOf(17, 18, 19, 20, 23, 24, 25, 26, 27, 28, 63, 64, 67, 68, 69, 70, 71, 72, 73, 74)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    in singleSpanPos -> 6
                    in doubleSpanPos -> 3
                    else -> 2
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StagesRecyclerViewAdapter(StageListener { stage ->
            viewModel.onStageClicked(stage)
        })
        binding.stagesRecyclerView.adapter = adapter
        //adapter.updateStages(viewModel.stages)

        viewModel.stages.observe(this.viewLifecycleOwner, Observer { stages ->
            stages?.let { adapter.submitList(stages) }
        })
        layoutManager = GridLayoutManager(activity, 6)
        setSpanSize(layoutManager)
        binding.stagesRecyclerView.layoutManager = layoutManager
        context?.let {
            binding.stagesRecyclerView.addItemDecoration(
                StagesDividerDecoration(
                    ContextCompat.getColor(
                        it,
                        R.color.progress_bar_background
                    ), resources.getDimensionPixelSize(R.dimen.divider_height), requireContext()
                )
            )
        }
    }

    private fun onClickStage() {
        viewModel.navigateToStageDialog.observe(viewLifecycleOwner, { stage ->
            val headers = listOf(0, 16, 22, 30, 47, 61, 65, 75, 91, 96)
            val hearts = AqsaLandmarksApplication.sharedPref.getString("hearts", "0")?.toInt()!!
            stage?.let {
                if (stage.id.toInt() !in headers && hearts > 0 && stage.id.toInt() < 48 && stage.passed) {
                    this.findNavController().navigate(
                        StagesFragmentDirections.actionStagesFragmentToStageDialogFragment(stage.id)
                    )
                    viewModel.onStageDialogNavigated()
                } else if (hearts == 0) {
                    Toast.makeText(activity, "للأسف نفذت فرصك!", Toast.LENGTH_LONG).show()
                    viewModel.onStageDialogNavigated()
                } else if (stage.id.toInt() >= 48) {
                    Toast.makeText(activity, "ستتوفر قريبًا؛ كن بالانتظار!", Toast.LENGTH_LONG)
                        .show()
                } else if (!stage.passed) {
                    Toast.makeText(
                        activity,
                        "قم بإتمام المراحل السابقة بنجمتين أو أكثر لتتمكن من فتح هذه المرحلة",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }


    private fun setProfileBackDrop() {
        user?.let {
            // Check if user's email is verified
            val emailVerified = user.isEmailVerified
            val name = user.displayName

            if (emailVerified) {
                binding.backDrop.user_name.text = name
            } else {
                binding.backDrop.user_name.text = "لم يتم تأكيد بريدك الإلكتروني"
            }
        }
        onClickSignOutButton(user)
        onClickSettingsButton(user)
        setBadgesRecyclerView()
    }

    private fun onClickSignOutButton(user: FirebaseUser?) {
        //val user = Firebase.auth.currentUser
        if (user != null) {
            binding.backDrop.sign_out_button.text = resources.getText(R.string.sign_out_button_text)
            binding.backDrop.sign_out_button.setOnClickListener {
                Firebase.auth.signOut()
                deleteUserData()
                Handler().postDelayed({
                    Toast.makeText(requireContext(), "لقد قمت بتسجيل الخروج", Toast.LENGTH_LONG)
                        .show()
                    findNavController().navigate(R.id.action_stagesFragment_to_openingFragment)
                }, 3000)
            }
        } else {
            binding.backDrop.sign_out_button.text = "تسجيل دخول أو انشاء حساب"
            binding.backDrop.sign_out_button.setOnClickListener {
                deleteUserData()
                findNavController().navigate(R.id.action_stagesFragment_to_openingFragment)

            }
        }
    }

    private fun deleteUserData() {
        animateDeletingUserData()
        deleteStagesUserData()
        deleteBadgesUserData()
        AqsaLandmarksApplication.sharedPref.edit().putInt("streakCounter", 0).apply()
        AqsaLandmarksApplication.sharedPref.edit().putInt("lastDay", 0).apply()
        AqsaLandmarksApplication.sharedPref.edit().putString("hearts", "5").apply()
    }

    private fun deleteStagesUserData() {
        viewModel.stages.observe(this.viewLifecycleOwner, Observer { stages ->
            stages?.let {
                for (stage in stages) {
                    if (stage.id == "1") {
                        stage.score = 0
                        stage.passed = true
                        viewModel.updateStages(stage)
                    } else {
                        stage.score = 0
                        stage.passed = false
                        viewModel.updateStages(stage)
                    }
                }
            }
        })
    }

    private fun deleteBadgesUserData() {
        viewModel.badges.observe(this.viewLifecycleOwner, Observer { badges ->
            badges?.let {
                for (badge in badges) {
                    badge.achieve = 0
                    viewModel.updateBadges(badge)
                    Timber.i(badge.toString())
                }
            }
        })

    }

    private fun onClickSettingsButton(user: FirebaseUser?) {
        if (user != null) {
            binding.backDrop.settings_button.visibility = View.VISIBLE
            binding.backDrop.settings_button.setOnClickListener {
                createDropDownMenu(user)
                //findNavController().navigate(R.id.action_stagesFragment_to_settingsDialogFragment)
            }
        } else {
            binding.backDrop.settings_button.visibility = View.GONE
        }
    }

    private fun createDropDownMenu(user: FirebaseUser) {

        val dropDownMenu = PopupMenu(context, binding.backDrop.settings_button)
        val menu = dropDownMenu.menu

        menu.add(0, 0, 0, "تعديل اسم المستخدم")
        menu.add(0, 1, 0, "تعديل كلمة السر")
        menu.add(0, 2, 0, "حذف الحساب")

        dropDownMenu.setOnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                0 -> {
                    editUserName(user)
                }
                1 -> {
                    editPassword(user)
                }
                2 -> {
                    deleteAccount(user)
                }
            }

            true
        }
        dropDownMenu.show()
    }

    private fun editUserName(user: FirebaseUser) {
        edit_user_name.visibility = View.VISIBLE
        user_name.visibility = View.GONE
        binding.backDrop.edit_user_name.setText(user.displayName)

        binding.backDrop.settings_button.visibility = View.GONE
        binding.backDrop.save_button.visibility = View.VISIBLE
        binding.backDrop.save_button.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {
                displayName = binding.backDrop.edit_user_name.text.toString()
            }
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("User profile updated to ${user.displayName}")
                        activity?.finish()
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                    }
                }

            binding.backDrop.settings_button.visibility = View.VISIBLE
            binding.backDrop.save_button.visibility = View.GONE
            binding.backDrop.user_name.visibility = View.VISIBLE
            binding.backDrop.edit_user_name.visibility = View.GONE
        }
    }

    private fun editPassword(user: FirebaseUser) {
        for (userInfo in FirebaseAuth.getInstance().currentUser!!.providerData) {
            if (userInfo.providerId == "password") {
                println("User is signed in with email/password")
                val dialog = ChangePasswordDialogFragment()
                dialog.show(childFragmentManager, "ChangePasswordDialogFragment")
            } else {
                Toast.makeText(
                    context,
                    "لا يمكنك نغيير كلمة السر لأنك قمت بتسجيل الدخول باستخدام جوجل",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteAccount(user: FirebaseUser) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = MaterialAlertDialogBuilder(
                requireContext(),
                R.style.ThemeOverlay_App_SettingsDialog
            )
            builder.setTitle("هل بالتأكيد تريد حذف حسابك؟")
                .setPositiveButton("نعم، حذف") { _, _ ->
                    for (userInfo in FirebaseAuth.getInstance().currentUser!!.providerData) {
                        if (userInfo.providerId == "password") {
                            println("User is signed in with email/password")

                            val dialog = DeleteAccountDialogFragment()
                            dialog.show(childFragmentManager, "DeleteAccountDialogFragment")
                            //findNavController().navigate(R.id.action_stagesFragment_to_deleteAccountDialogFragment)
                        } else {
                            rootRef.child(user.uid).removeValue()
                            user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        deleteUserData()
                                        Timber.d("User account deleted.")
                                        Handler().postDelayed({
                                            Toast.makeText(
                                                context, "تم حذف الحساب بنجاح",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            activity?.finish()
                                            startActivity(
                                                Intent(
                                                    activity,
                                                    MainActivity::class.java
                                                )
                                            )
                                        }, 5000)
                                    } else {
                                        Toast.makeText(
                                            context, "حدثت مشكلة أثناء حذف الحساب",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    }
                }
                .setNegativeButton("الغاء") { dialog, _ ->
                    dialog.cancel()
                    dialog.dismiss()
                }
                .create()
        }
        alertDialog?.show()
    }


    private fun animateDeletingUserData() {
        binding.progressBar.alpha = 0f
        binding.progressBar.visibility = View.VISIBLE

        val alphaAnimator = ValueAnimator.ofFloat(0f, 1f)
        alphaAnimator.duration = 1000

        alphaAnimator.addUpdateListener {
            val animationAlpha = it.animatedValue as Float
            binding.progressBar.alpha = animationAlpha
            binding.stagesRecyclerView.alpha = 1 - animationAlpha
        }

        alphaAnimator.start()
    }


    private fun setBadgesRecyclerView() {
        badgesAdapter = BadgesRecyclerViewAdapter()
        binding.backDrop.badges_recyclerview.adapter = badgesAdapter
        viewModel.badges.observe(this.viewLifecycleOwner, Observer { badges ->
            badges?.let { badgesAdapter.submitList(badges) }
        })
        badgesLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.backDrop.badges_recyclerview.layoutManager = badgesLayoutManager
    }

    private fun setStreakColor() {

        val calendar = Calendar.getInstance()
        val thisDay: Int = calendar.get(Calendar.DAY_OF_YEAR) // GET THE CURRENT DAY OF THE YEAR
        val lastDay = AqsaLandmarksApplication.sharedPref.getInt(
            "lastDay",
            0
        ) //If we don't have a saved value, use 0.

        if (lastDay == thisDay) {
            binding.streak.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.streak_icon_white_24, 0
            )
            binding.streak.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            binding.streak.setCompoundDrawablesRelativeWithIntrinsicBounds(
                0, 0, R.drawable.streak_icon_green_24, 0
            )
            binding.streak.setTextColor(resources.getColor(R.color.progress_bar_background))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//        if (id == R.id.profile_item) {
//            // Do something
//
//            return true
//        }
//        return if (id == R.id.streak_item) {
//
//            // Do something
//            true
//        } else super.onOptionsItemSelected(item)
//    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "تنبيه عند استعادة الفرص"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }
        // TODO: Step 1.6 END create a channel
    }

}