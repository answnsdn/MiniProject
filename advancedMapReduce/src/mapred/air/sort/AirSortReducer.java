package mapred.air.sort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

//input data 하나마다 실행됨
public class AirSortReducer
	extends Reducer<CustomKey, IntWritable,CustomKey,IntWritable>{
	IntWritable resultVal = new IntWritable();
	CustomKey resultkey = new CustomKey();
	@Override
	protected void reduce(CustomKey key,
			Iterable<IntWritable> values,
			Reducer<CustomKey, IntWritable, CustomKey, IntWritable>.Context context) throws IOException, InterruptedException {
		int sum = 0;
		//CustomKey 데이터 중 month데이터를 추출
		Integer beforeMonth = key.getMonth();
		for (IntWritable value:values) {
			if(beforeMonth!=key.getMonth()) {
				resultkey.setMonth(beforeMonth);
				resultkey.setYear(key.getYear());;
				resultVal.set(sum);
				context.write(resultkey, resultVal);
				sum=0;
			}
			sum = sum+value.get();
			beforeMonth = key.getMonth();
		}                              
		//values에 전달된 값들을 반복 작업
		//마지막에 같은 키를 갖고 있는 값을 한꺼번에 출력 - 키(year,month)가 같은 경우
		if(key.getMonth()==beforeMonth) {
			resultkey.setMonth(key.getMonth());
			resultkey.setYear(key.getYear());;
			resultVal.set(sum);
			context.write(resultkey, resultVal);		
		}
	}
}







