package voxspell.toolbox;

public class Words implements Comparable<Words>{
	
	
	protected int mastered;
	protected int faulted;
	protected int failed;
	protected String name;
	
	public Words(int i, int j, int k, String s){
		mastered = i;
		faulted = j;
		failed = k;
		name = s;
	}
	
	@Override
	public String toString(){
		return name;
	}
	
	public boolean equalsTo(Words w){
		if ((mastered==w.mastered)&&(faulted==w.faulted)&&(failed==w.failed)){
			return true;
		}
		return false;
	}
	
	@Override
	public int compareTo(Words w) {
		if (this.equalsTo(w)){
			return 0;
		}else{
			if (mastered>w.mastered){
				return 1;
			}else if (mastered==w.mastered){
				if (failed>w.failed){
					return -1;
				}else if (failed==w.failed){
					if (faulted>w.faulted){
						return -1;
					}else{
						return 1;
					}
				}else{
					return 1;
				}
			}
		}
		return -1;
	}
}
