# CMPT 362 My run 1 

Our Task :

Complete the profile activity. Save the profile to the phone and reload when needed.

This is the first in a series of labs that allow you to develop the MyRuns App to capture and display your physical activities using your Android phone. This lab focuses on developing a simple UI for setting up your profile: i.e. name, email, phone number, gender, and major. It works as follows: the app presents the user with an activity that allows them to input and save their profile. When the app is opened again, the saved profile information should be reloaded and displayed, allowing the user to review their data and make further changes if needed.

In this lab, there is a single activity. The UI design for the activity is specified in the Settings Tab section, and the implementation design is specified in the Settings Fragment section. The application is defined in the AndroidManifest.xml file. You specify all of these files in XML and Kotlin code. Recall the manifest captures the key information about the application to the Android system, information the system needs before it can run any of the application's code -- for example, the activity name, etc. Typically, you will update the manifest, for example, if you have an application with more than one activity.

Note: since DialogFragment has not been taught at this point, you do not have to implement the selection of the profile image through "select from gallery". When the "Change" button is clicked, you only need to take a picture using the camera.

Please be aware that android:configChanges="keyboardHidden|orientation" is not allowed in MyRuns.

You can use println for logging purposes with Android -- for control flow or looking at programming states such as variables. Alternatively, you can use Log.d(TAG, ..). Setup your on TAG and add Log.d()s to all your methods. If your program crashes, don’t panic. Look into the system log in the LogCat window, it will print out the function call stack upon crash. If you see logs with red font, then that is associated with the exception. Most of the time you will find what causes your crash. We will discuss debugging techniques in more detail next week in class.

Given the nature of the assignments, you might have questions about how much you should or shouldn't do. The answer is you have to do your best to provide us with a working and bug-free version of the demo apps.

For assignments and grading purposes, the apps you turn in should work under "normal and natural" conditions when used by a user. E.g., if the app crashes between taking a photo and saving it, then it is failing under the expected conditions for use. However, if the app crashes when the user is operating with some exceptional or unexpected conditions, then it is acceptable (i.e., you won't be penalized in this case), e.g., permissions, it is expected that permissions should be granted for normal operation of the app, however, if the user doesn't grant permissions, then it is not a normal operation from the user's side, and the app might crash. In this case, you won't be penalized if the app crashes.

Further, we leave it to your judgment to decide what falls under expected or unexpected use cases. Once in a while, it is OK to clarify with us, but if asking about what to do becomes too frequent, we might choose not to answer. The purpose of this is to make you think about how real-world app development works instead of handing you a list of items to check off.

Ideally, you would want to take into account and fix any crashes because of unexpected use cases, but as I mentioned, you will not be penalized if you don't.
