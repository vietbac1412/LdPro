package tamhoang.ldpro4.util.algorithm

/**
 * @author  : Pos365
 * @Skype   : chukimmuoi
 * @Mobile  : +84 167 367 2505
 * @Email   : chukimmuoi@gmail.com
 * @Website : https://cafe365.pos365.vn/
 * @Project : CashiersPos365
 * Created by chukimmuoi on 22/09/2018.
 */
// https://github.com/gazolla/Kotlin-Algorithm/tree/master/Queue

class Queue<T>(list: MutableList<T>) {
    var items: MutableList<T> = list

    fun isEmpty(): Boolean = this.items.isEmpty()

    fun count(): Int = this.items.count()

    override fun toString() = this.items.toString()

    fun enqueue(element: T) {
        this.items.add(element)
    }

    fun dequeue(): T? {
        return if (this.isEmpty()) {
            null
        } else {
            this.items.removeAt(0)
        }
    }

    fun peek(): T? {
        return this.items[0]
    }
}