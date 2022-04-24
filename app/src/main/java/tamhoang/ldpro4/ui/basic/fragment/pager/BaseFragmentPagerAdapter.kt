package tamhoang.ldpro4.ui.basic.fragment.pager

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import tamhoang.ldpro4pos365.ui.basic.fragment.BaseFragment

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 10/03/2018.
 */
/**
 * Class de lu tru tat ca cac fragment nam trong viewpager va quan ly vong doi cua chung
 *
 * Co 2 loai pager adapter [FragmentStatePagerAdapter] & [FragmentPagerAdapter]
 * O day dung [FragmentStatePagerAdapter] cho muc dich cap nhat va thay the fragment runtime
 * */
// Link tham khao
// https://github.com/codepath/android_guides/wiki/ViewPager-with-FragmentPagerAdapter
// https://gist.github.com/nesquena/c715c9b22fb873b1d259
abstract class BaseFragmentPagerAdapter(fragmentManager: FragmentManager)
    : FragmentStatePagerAdapter(fragmentManager) {

    private var mListRegisteredFragments: SparseArray<BaseFragment> = SparseArray()

    // Co 2 cach de update lai list fragment
    // Cach 1: Update bang cach set 1 list tu ben ngoai
    fun setListRegisteredFragments(fragments: List<BaseFragment>) {
        mListRegisteredFragments.clear()

        val size = fragments.size
        for (i in 0 until size) {
            val fragment = fragments[i]
            mListRegisteredFragments.put(i, fragment)
        }

        notifyDataSetChanged()
    }

    // Cach 2: Tu dong them vao tung fragment khi class ke thua goi getItem() va tra ve fragment moi
    // Neu dung cach nay nen ghi de method getItemCount()
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as BaseFragment
        mListRegisteredFragments.put(position, fragment)
        return fragment
    }

    // Co chuc nang giong voi method getItem()
    fun getRegisteredFragment(position: Int): BaseFragment? {
        return mListRegisteredFragments[position]
    }

    override fun getItem(position: Int): Fragment {
        return mListRegisteredFragments[position]
    }

    override fun getCount(): Int {
        return mListRegisteredFragments.size()
    }

    /**
     * Phuong phap nay se duoc goi cho moi fragment cua ViewPager.
     *
     * @return POSITION_NONE - bat buoc phai tai lai - cho phep update khi goi [notifyDataSetChanged]
     *      or POSITION_UNCHANGED - khong bat buoc phai tai lai
     * */
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        mListRegisteredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun saveState(): Parcelable? {
        return super.saveState()
    }
}