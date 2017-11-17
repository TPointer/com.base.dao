package alldao.dao;

public interface BaseDao<T> {

	void add(T t);
	void del(T t);
	void update(T t);
	
	T selectById(T t);
	
	
}
