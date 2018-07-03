package typeofmood.ime;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import typeofmood.ime.datahandler.MoodDatabaseHelper;

public class MainActivityCalendarFragment extends Fragment {
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.calendar_fragment, container, false);

        final ListView listView = rootView.findViewById(R.id.moodListView);
        CalendarView calendarView= rootView.findViewById(R.id.calendarView);
        if(calendarView!=null){
            calendarView.dispatchSetSelected(true);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String mDay=Integer.toString(dayOfMonth);
                    String mMonth=Integer.toString((month+1));
                    String mYear=Integer.toString(year);
                    if(mDay.length()==1){ //solving the problem of 1-6-2018 not resolving to 01-06-2018
                        mDay="0"+mDay;
                    }
                    if(mMonth.length()==1){
                        mMonth="0"+mMonth;
                    }
                    String date=mYear+"-"+mMonth+"-"+mDay;
                    if(listView != null) {
                        MoodDatabaseHelper myDB;
                        myDB=new MoodDatabaseHelper(getActivity().getApplicationContext());
                        ArrayList<String> theList = new ArrayList<>();
                        Cursor data = myDB.getListDateContentsMood(date);
                        Cursor data2 = myDB.getListDateContentsPhysical(date);
                        if (data.getCount() == 0 && data2.getCount()==0) {
                            Toast.makeText(getActivity(), "There are no recorded emotion states at "+mDay+"-"+mMonth+"-"+mYear, Toast.LENGTH_SHORT).show();
                            theList.clear();
                            ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, theList);
                            listView.setAdapter(listAdapter);
                        } else {
                            while (data.moveToNext()) {
                                String strData2="";
                                if(data2.moveToNext()){
                                    String state=data2.getString(2);
                                    if(state.equals("Relaxation")){
                                        strData2=" and "+"Relaxed";
                                    }else if(state.equals("Tiredness")){
                                        strData2=" and "+"Tired";
                                    }else if(state.equals("Sickness")){
                                        strData2=" and "+"Sick";
                                    }
                                }
                                theList.add("At "+data.getString(4)+"\n"+"You were feeling "+data.getString(1)+strData2);
                                ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, theList);
                                listView.setAdapter(listAdapter);
                            }

                        }
                    } else {
                        Log.d("asd", "list_of_stops not found");
                    }
                }
            });
        }


        return rootView;

}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }



}
