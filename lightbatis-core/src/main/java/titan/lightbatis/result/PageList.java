/**
 * 
 */
package titan.lightbatis.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lifei
 *
 */
@Data
@ApiModel(value="带分页查询的数据列表", description = "继承 List 接口，增加了总条数的属性")
public class PageList<E> implements List<E> {
	
	private List<E> records = new ArrayList<>();
	@ApiModelProperty(value="本次查询的数据总条数")
	private int totalSize = 0;

	public PageList() {
	}
	

	public int size() {
		return records.size();
	}

	public boolean isEmpty() {
		return records.isEmpty();
	}

	public boolean contains(Object o) {
		return records.contains(o);
	}

	public Iterator<E> iterator() {
		return records.iterator();
	}

	public Object[] toArray() {
		return records.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return records.toArray(a);
	}

	public boolean add(E e) {
		return records.add(e);
	}

	public boolean remove(Object o) {
		return records.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return records.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		return records.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return records.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return records.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return records.retainAll(c);
	}


	public void clear() {
		records.clear();
	}

	public boolean equals(Object o) {
		return records.equals(o);
	}

	public int hashCode() {
		return records.hashCode();
	}

	public E get(int index) {
		return records.get(index);
	}

	public E set(int index, E element) {
		return records.set(index, element);
	}

	public void add(int index, E element) {
		records.add(index, element);
	}


	public E remove(int index) {
		return records.remove(index);
	}


	public int indexOf(Object o) {
		return records.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return records.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return records.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return records.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return records.subList(fromIndex, toIndex);
	}

	
}
