package edu.purdue.cs.range;

public class RangeQuery {
	int id; 
	int xmin,ymin,xmax,ymax;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getXmin() {
		return xmin;
	}
	public void setXmin(int xmin) {
		this.xmin = xmin;
	}
	public int getYmin() {
		return ymin;
	}
	public void setYmin(int ymin) {
		this.ymin = ymin;
	}
	public int getXmax() {
		return xmax;
	}
	public void setXmax(int xmax) {
		this.xmax = xmax;
	}
	public int getYmax() {
		return ymax;
	}
	public void setYmax(int ymax) {
		this.ymax = ymax;
	}
	public RangeQuery(int id, int xmin, int ymin, int xmax, int ymax) {
		super();
		this.id = id;
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}
	public boolean filterPoint(int x,int y){
		if(x>=xmin&&x<=xmax&&y>=ymin&&y<=ymax)
			return true;
		return false;
	}

}
