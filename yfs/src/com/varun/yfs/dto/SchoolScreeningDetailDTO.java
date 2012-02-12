package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SchoolScreeningDetailDTO extends BaseModelData
{
	private static final long serialVersionUID = 7504451639139702860L;

	private long id;
	private CountryDTO country;
	private StateDTO state;
	private CityDTO city;
	private TownDTO town;
	private VillageDTO village;
	private ChapterNameDTO chapterName;
	private LocalityDTO locality;
	private ProcessTypeDTO processType;
	private TypeOfLocationDTO typeOfLocation;
	private List<VolunteerDTO> lstVolunteers;
	private List<DoctorDTO> lstDoctors;
	private List<SchoolPatientDetailDTO> lstPatientDetails;

	public SchoolScreeningDetailDTO()
	{
		setDeleted("N");
	}

	public SchoolScreeningDetailDTO(String name)
	{
		setDeleted("N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		set("id", id);
		this.id = id;
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public final void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public CountryDTO getCountry()
	{
		return get("country");
	}

	public void setCountry(CountryDTO country)
	{
		set("country", country);
	}

	public StateDTO getState()
	{
		return get("state");
	}

	public void setState(StateDTO state)
	{
		set("state", state);
	}

	public CityDTO getCity()
	{
		return get("city");
	}

	public void setCity(CityDTO city)
	{
		set("city", city);
	}

	public TownDTO getTown()
	{
		return get("town");
	}

	public void setTown(TownDTO town)
	{
		set("town", town);
	}

	public VillageDTO getVillage()
	{
		return get("village");
	}

	public void setVillage(VillageDTO village)
	{
		set("village", village);
	}

	public ChapterNameDTO getChapterName()
	{
		return get("chapterName");
	}

	public void setChapterName(ChapterNameDTO chapterName)
	{
		set("chapterName", chapterName);
	}

	public LocalityDTO getLocality()
	{
		return get("locality");
	}

	public void setLocality(LocalityDTO locality)
	{
		set("locality", locality);
	}

	public String getAddress()
	{
		return get("address");
	}

	public void setAddress(String address)
	{
		set("address", address);
	}

	public String getScreeningDate()
	{
		return get("screeningDate");
	}

	public void setScreeningDate(String screeningDate)
	{
		set("screeningDate", screeningDate);
	}

	public ProcessTypeDTO getProcessType()
	{
		return get("processType");
	}

	public void setProcessType(ProcessTypeDTO processType)
	{
		set("processType", processType);
	}

	public TypeOfLocationDTO getTypeOfLocation()
	{
		return get("typeOfLocation");
	}

	public void setTypeOfLocation(TypeOfLocationDTO typeOfLocation)
	{
		set("typeOfLocation", typeOfLocation);
	}

	public List<VolunteerDTO> getVolunteers()
	{
		return get("lstVolunteers");
	}

	public void setVolunteers(List<VolunteerDTO> setVolunteers)
	{
		set("volunteers", setVolunteers);
	}

	public List<DoctorDTO> getDoctors()
	{
		return get("lstDoctors");
	}

	public void setDoctors(List<DoctorDTO> setDoctors)
	{
		set("doctors", setDoctors);
	}

	public String getContactInformation()
	{
		return get("contactInformation");
	}

	public void setContactInformation(String contactInformation)
	{
		set("contactInformation", contactInformation);
	}

	public void setPatientDetails(List<SchoolPatientDetailDTO> lstPatientDetails)
	{
		set("lstPatientDetails", lstPatientDetails);
	}

	public List<SchoolPatientDetailDTO> getPatientDetails()
	{
		return get("lstPatientDetails");
	}

	@Override
	public String toString()
	{
		return get("name");
	}

}
