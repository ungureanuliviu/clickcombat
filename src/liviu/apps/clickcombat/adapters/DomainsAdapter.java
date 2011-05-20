package liviu.apps.clickcombat.adapters;

import java.util.ArrayList;

import liviu.apps.clickcombat.R;
import liviu.apps.clickcombat.data.Domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DomainsAdapter extends BaseAdapter{
	
	// constants
	private final String 		TAG = "DomainsAdapter";
	
	
	// data
	private LayoutInflater 		lf;
	private ArrayList<Domain>	items;
	
	
	public DomainsAdapter(Context context) {
		items 	= new ArrayList<Domain>();
		lf		= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public DomainsAdapter addItem(Domain d){
		items.add(d);
		return this;
	}
	
	public int getCount() {
		return items.size();
	}

	public Domain getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		
		if(convertView == null){			
			convertView = lf.inflate(R.layout.main_list_item_layout, parent, false);
			vh 			= new ViewHolder();
			vh.name		= (TextView)convertView.findViewById(R.id.main_list_item_name);
			vh.desc 	= (TextView)convertView.findViewById(R.id.main_list_item_desc);
			
			convertView.setTag(vh);
		}
		else
			vh = (ViewHolder)convertView.getTag();
		
		vh.name.setText(items.get(position).getName());
		vh.name.setTextSize(items.get(position).getPx());
		vh.desc.setText(items.get(position).getDescription());
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView name;
		public TextView desc;
		
		public ViewHolder() {}
	}

	public void clear() {		
		items.clear();
	}

}
