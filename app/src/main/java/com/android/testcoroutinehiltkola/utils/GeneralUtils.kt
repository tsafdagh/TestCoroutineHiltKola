package com.android.testcoroutinehiltkola.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.database.Cursor
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.testcoroutinehiltkola.R
import com.android.testcoroutinehiltkola.enums.ENUMCATEGORYEXPENSE
import com.android.testcoroutinehiltkola.model.CashFlow
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


private const val TAG = "GeneralUtils"
const val ORANGE_OPERATOR_NAME = "Orange"
const val ORANGE_MONEY_NAME = "OrangeMoney"
const val MTN_OPERATOR_NAME = "MTN"
const val MTN_Money_NAME = "MobileMoney"
const val BOTH_OPERATOR_NAME = "BOTH"
const val BALANCE_ALL = "ALL"
const val BALANCE_PERCENT = "BALANCE_PERCENT"
const val BALANCE_FIX = "BALANCE_FIX"
const val WHATSAP_TEST_LINK = "https://chat.whatsapp.com/C42q8u2TDtsC7Ewyh6WOnE"

fun printlnApp(message: String){
    Log.d("AppDebug", message)
}
/**
 * to get a month name from a ont provided by the date class
 * @param month the number of the month
 * @return String the name of the month in english
 */
fun getMontNameFromInt(month: Int): String {
    val formatter = SimpleDateFormat("MMMM", Locale.getDefault())
    val calendar = GregorianCalendar()
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.MONTH, month)
    return formatter.format(calendar.getTime()).capitalize()
    //return DateFormatSymbols().months[month]
}



fun isOrangeMoneyTransfertInitiated(messageFromUSSD: String): Boolean {
    val regexForOrangeMoneyTransferInitiated =
        """transfert initie|transfert est initie|transfer initiated|transfer is initiated|successfully transferred|effectué avec succes|rechargement effectue|top up done""".toRegex()
    val lowerCaseMessage = messageFromUSSD.toLowerCase()
    return lowerCaseMessage.contains(regexForOrangeMoneyTransferInitiated)
}

fun isMTNMoneyTransfertInitiated(messageFromUSSD: String): Boolean {
    val regexForMTNMoneyTransferInitiated =
        """transfert effectue avec succes|successful transfer of|successful transfer|transaction is pending|transaction est en cours|transaction en cours|transfert reussi""".toRegex()
    val lowerCaseMessage = messageFromUSSD.toLowerCase()
    return lowerCaseMessage.contains(regexForMTNMoneyTransferInitiated)
}

fun getGMTDate(): String {
    val dateFormatGmt = SimpleDateFormat("dd:MM:yyyy HH:mm:ss", Locale.getDefault())
    dateFormatGmt.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormatGmt.format(Date())
}

/**
 * to format double 2 number after the coma
 * @param number the double you want to format
 * @author Yvana
 */
fun Double.formatDouble(): Double {
    try {
        val df = DecimalFormat("#.##")
        val result = df.format(this)
        val res = result.replace(",", ".")
        return res.toDouble()
    } catch (e: NumberFormatException) {
        return this
    }
}

/**
 * this function is for format the number with space between thousands to be more understanding
 * @author yvana
 * @return String
 */
fun Double.formatNumberWithSpaceBetweenThousand(): String {
    val string = DecimalFormat("###,###.##", DecimalFormatSymbols(Locale.ENGLISH)).format(this)
    return string.replace(",", " ")
}


/**
 * this function is for format the number with space between thousands to be more understanding
 * @author yvana
 * @return String
 */
fun Double.formatNumberWithSpaceBetweenThousandAndTakeIntegerPart(): String {
    val value = if (this >= 1000000.0) {
        this.toInt().toDouble()
    } else {
        this
    }
    return value.formatNumberWithSpaceBetweenThousand()
}

/**
 * the function to format phone number with space between 3 characters
 * @author yvana
 */

fun formatPhoneNumberWithSpace(phoneNumber: String): String {
    var numberSpaced = ""
    if (phoneNumber.length == 9) {
        numberSpaced =
            phoneNumber.substring(0, 1) + " " + phoneNumber.substring(
                1,
                3
            ) + " " + phoneNumber.substring(
                3,
                5
            ) + " " + phoneNumber.substring(
                5,
                7
            ) + " " + phoneNumber.substring(7)
    } else if (phoneNumber.length == 8) {
        numberSpaced =
            phoneNumber.substring(
                0,
                2
            ) + " " + phoneNumber.substring(
                2,
                4
            ) + " " + phoneNumber.substring(
                4,
                6
            ) + " " + phoneNumber.substring(6)
    } else if (phoneNumber.length == 13 && phoneNumber.startsWith("+237")) {
        numberSpaced =
            phoneNumber.substring(0, 4) + " " + phoneNumber.substring(
                4,
                5
            ) + " " + phoneNumber.substring(
                5,
                7
            ) + " " + phoneNumber.substring(
                7,
                9
            ) + " " + phoneNumber.substring(
                9,
                11
            ) + " " + phoneNumber.substring(11)
    } else if (phoneNumber.length == 12 && phoneNumber.startsWith("+237")) {
        numberSpaced =
            phoneNumber.substring(0, 4) + " " + phoneNumber.substring(
                4,
                6
            ) + " " + phoneNumber.substring(
                6,
                8
            ) + " " + phoneNumber.substring(
                8,
                10
            ) + " " + phoneNumber.substring(10)
    } else {
        numberSpaced = phoneNumber
    }
    return numberSpaced
}

fun getVersionNameOfApp(context: Context): String {
    val pinfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return pinfo.versionName
}

fun getVersionCodeOfApp(context: Context): Long {
    val pinfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pinfo.longVersionCode
    } else {
        pinfo.versionCode.toLong()
    }
}

/**
 * Cette fonction a pour rôle de retourner
 * l'EMEIl du telephone de l'utilisateur
 * @param context: le context actuel qui execute le service
 * @return phone EMEI or empty string if
 * a permission is not granded by user
 */
/**
 * this function retuns the EMEII of the user's phone.
 * @param context the context that execute the service
 *
 */
@SuppressLint("HardwareIds")
fun getDeviceId(
    context: Context
): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

fun getDeviceModel(): String {
    return android.os.Build.MODEL
}

fun getDevice(): String {
    return android.os.Build.DEVICE
}

fun Fragment.log(tag: String, message: String) {
    Log.e(tag, message)
}

fun getAndroidSDKVersion(): Int {
    return android.os.Build.VERSION.SDK_INT
}

/**
 * This function takes a vector resource id and return a bitmap from it
 * This function just create a bitmap from a vector that is in the resource of the application
 * @param context to get the drawable
 * @param vectorResId the id of the drawable
 * @return Bitmap the bitmap drawn with the vector
 * @author Yvana
 */
fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): Bitmap {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable!!.setBounds(0, 0, 250, 250)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.getIntrinsicWidth(),
        vectorDrawable.getIntrinsicHeight(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return Bitmap.createBitmap(bitmap)
}

/**
 * this function just resize a bitmap
 * @param bm the old bitmap you want to resize
 * @param newWidth the new width you want
 * @param newHeight the new height you want
 * @return Bitmap the new bitmap resized
 * @param Yvana
 */
fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    val width = bm.width
    val height = bm.height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    // CREATE A MATRIX FOR THE MANIPULATION
    val matrix = Matrix()
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight)

    // "RECREATE" THE NEW BITMAP
    try {
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    } catch (e: java.lang.Exception) {
        return bm
    }
}

fun getRealPathFromUri(
    context: Context,
    contentUri: Uri?
): String? {
    var cursor: Cursor? = null
    return try {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        cursor.getString(column_index)
    } finally {
        cursor?.close()
    }
}
fun getCasflowTotal(list: List<CashFlow>): HashMap<String, Double> {
    var result = HashMap<String, Double>()
    var totalIncomes = 0.0
    var TotalOutcomes = 0.0
    list.forEach {
        if (it.category?.equals(ENUMCATEGORYEXPENSE.INCOME) ?: false) {
            totalIncomes += it.amount
        } else {
            TotalOutcomes += it.amount
        }
    }
    result.put("income", totalIncomes)
    result.put("outcome", TotalOutcomes)
    result.put("total", totalIncomes + TotalOutcomes)
    return result
}

fun getImageContentUri(
    context: Context,
    imageFile: File
): Uri? {
    val filePath = imageFile.absolutePath
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        arrayOf(MediaStore.Images.Media._ID),
        MediaStore.Images.Media.DATA + "=? ",
        arrayOf(filePath),
        null
    )
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(
            cursor
                .getColumnIndex(MediaStore.MediaColumns._ID)
        )
        val baseUri =
            Uri.parse("content://media/external/images/media")
        Uri.withAppendedPath(baseUri, "" + id)
    } else {
        if (imageFile.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, filePath)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
        } else {
            null
        }
    }
}

fun getDaysFromPeriod(period: String): Int {
    when (period) {
        "weekly" -> return 6
        "Monthly" -> return 30
        "trimester" -> return 90
        "semester" -> return 180
        "annual" -> return 365
        else -> return 6
    }
}

fun shareInvitation(title: String, subjet: String, context: Context) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.putExtra(Intent.EXTRA_TEXT, "$title $subjet")
    intent.type = "text/plain"
    context.startActivity(intent)
}

/**
 * this function is for design and display the action bar in the fragment with a title, and a background
 * To add a menu in the action bar we must add the function setHasOptionMenu(true) in the fragment
 * @author Yvana
 * @param activity the current activity
 * @param title the title of your fragment
 * @param background the drawable that you want to display in the actionBar
 * @param hasHomeMenu state if the fragment has or not the home button. the default is true.
 * @return ActionBar the function return the action bar created if we want specifics adding...
 */
fun defineActionbar(
    activity: AppCompatActivity,
    title: String,
    background: Drawable,
    hasHomeMenu: Boolean = true,
    elevationValue: Float = 0F
): ActionBar {
    return activity.supportActionBar!!.apply {
        setTitle(title)
        elevation = elevationValue
        setBackgroundDrawable(background)
        setDisplayHomeAsUpEnabled(hasHomeMenu)
        //setDisplayShowHomeEnabled(hasHomeMenu)
        show()
    }
}

/**
 * Sometimes we want to display a number but the number is too long and we can display it
 * with k at the end if it is hundred number and m if it is a million number.
 * This function divide the number to know if the number is a hundred or a million number
 * @author Yvana
 * @param number the number to format
 * @return String
 */
fun formatLongNumber(number: Double): String {
    var numberString = ""
    if (Math.abs(number / 1000000000) > 1) {
        numberString = (number / 1000000000).formatDouble().removeZeroAtEnd() + "b"
    } else if (Math.abs(number / 1000000) > 1) {
        numberString = (number / 1000000).formatDouble().removeZeroAtEnd() + "m"

    } else if (Math.abs(number / 1000) > 1) {
        numberString = (number / 1000).formatDouble().removeZeroAtEnd() + "k"

    } else {
        numberString = number.formatDouble().removeZeroAtEnd()

    }
    return numberString
}

fun formatLongLongNumber(number: Double): String {
    if (number >= 100000) {
        var numberString = ""
        if (Math.abs(number / 1000000) > 1) {
            numberString = (number / 1000000).formatDouble().removeZeroAtEnd() + "m"

        } else if (Math.abs(number / 1000) > 1) {
            numberString = (number / 1000).formatDouble().removeZeroAtEnd() + "k"

        } else {
            numberString = number.formatNumberWithSpaceBetweenThousand()

        }
        return numberString
    } else {
        return number.formatNumberWithSpaceBetweenThousand()
    }

}

fun formatDate(sendingDate: Date, context: Context): String {
    val todayDate = convertDateToSpecificStringFormat(Calendar.getInstance().time, "dd MMM yyy")
    val sendDate = convertDateToSpecificStringFormat(sendingDate, "dd MMM yyy")
    if (todayDate == sendDate) {
        return "${context.getString(R.string.today)}: ${convertDateToSpecificStringFormat(
            sendingDate,
            "kk:mm"
        )}"
    }
    return convertDateToSpecificStringFormat(sendingDate)
}

/**
 * to format date to a specific format like have only the 3 first letters of a month to have custom date
 * @param date the date to format
 * @param format the format of date that who want
 * @return the date formatted
 */
fun convertDateToSpecificStringFormat(date: Date, format: String = "dd MMM yyyy kk:mm"): String {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(date)
}

fun Uri.getName(context: Context): String {
    val returnCursor = context.contentResolver.query(this, null, null, null, null)
    var nameIndex: Int = 0
    try {
        nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    } catch (e: Exception) {
        val randomName = UUID.randomUUID().toString()
        return randomName.substring(randomName.length - 15, randomName.length) + ".pdf"
    }
    returnCursor.moveToFirst()
    val fileName = returnCursor.getString(nameIndex)
    returnCursor.close()
    return fileName
}


