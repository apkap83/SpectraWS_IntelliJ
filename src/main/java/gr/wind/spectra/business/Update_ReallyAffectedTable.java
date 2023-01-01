package gr.wind.spectra.business;

import java.sql.SQLException;

public class Update_ReallyAffectedTable extends Thread
{
	iStatic_DB_Operations s_dbs;
	String foundIncidentID;
	String allAffectedServices;
	String foundScheduled;
	String CLIProvided;
	private String tablePrefix;
	private final String windTableNamePrefix = "";
	private final String novaTableNamePrefix = "Nova_";

	public Update_ReallyAffectedTable(iStatic_DB_Operations s_dbs, String foundIncidentID, String allAffectedServices,
			String foundScheduled, String CLIProvided)
	{
		this.s_dbs = s_dbs;
		this.foundIncidentID = foundIncidentID;
		this.allAffectedServices = allAffectedServices;
		this.foundScheduled = foundScheduled;
		this.CLIProvided = CLIProvided;

		// Check if Export is for Nova or Wind
		if (s_dbs.getClass().toString().equals("class gr.wind.spectra.business.s_DB_Operations")) {
			this.tablePrefix = windTableNamePrefix;
		} else if (s_dbs.getClass().toString().equals("class gr.wind.spectra.business.TnovaStaticDBOperations")) {
			this.tablePrefix = novaTableNamePrefix;

		}
	}

	@Override
	public void run()
	{
		String numOfTimesCliCalledForIncident = "0";

		// Check if CLI for this Incident exists
		// Check if we have at least one OPEN incident
		try
		{
			numOfTimesCliCalledForIncident = s_dbs.numberOfRowsFound(tablePrefix + "Stats_Pos_NLU_Requests",
					new String[] { "IncidentID", "CliValue" }, new String[] { foundIncidentID, CLIProvided },
					new String[] { "String", "String" });

			// If CLI has not called again then insert line
			if (numOfTimesCliCalledForIncident.equals("0"))
			{
				s_dbs.insertValuesInTable(tablePrefix + "Stats_Pos_NLU_Requests",
						new String[] { "IncidentID", "AffectedService", "Scheduled", "CliValue" },
						new String[] { foundIncidentID, allAffectedServices, foundScheduled, CLIProvided },
						new String[] { "String", "String", "String", "String", "String" });
			}
			// CLI has called again for this specific incident
			else
			{
				// Update value using LAST_INSERT_ID method of MySQL e.g. SET ModifyOutage = LAST_INSERT_ID(ModifyOutage+1)
				s_dbs.updateValuesBasedOnLastInsertID(tablePrefix + "Stats_Pos_NLU_Requests", "TimesCalled",
						new String[] { "IncidentID", "CliValue" }, new String[] { foundIncidentID, CLIProvided },
						new String[] { "String", "String" });

			}

		} catch (SQLException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
