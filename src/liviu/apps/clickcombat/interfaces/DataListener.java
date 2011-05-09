package liviu.apps.clickcombat.interfaces;

import java.util.ArrayList;

import liviu.apps.clickcombat.data.Domain;

public interface DataListener {
	public void onAllDomainsLoaded(ArrayList<Domain> domainsList);
}
