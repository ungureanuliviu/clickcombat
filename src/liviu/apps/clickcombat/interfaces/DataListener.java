package liviu.apps.clickcombat.interfaces;

import java.util.ArrayList;

import liviu.apps.clickcombat.data.Domain;

public interface DataListener {
	public void onAllDomainsLoaded(ArrayList<Domain> domainsList);
	public void onPagesCountLoaded(int pagesCount);
	public void onPageLoaded(int pageIndex, ArrayList<Domain> domains);
	public void onLatestDomainsLoaded(ArrayList<Domain> latestDomains);
}
