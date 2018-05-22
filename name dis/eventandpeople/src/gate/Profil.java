package gate;

public class Profil {

  private String name;

  private String email;

  private String organization;

  private String location;

  // constructeur
  public Profil() {
  }

  public Profil(String n, String e, String o, String l) {
    this.name = n;
    this.email = e;
    this.organization = o;
    this.location = l;
  }

  // getters & setters
  public String getName() {
    return name;
  }

  public String getEMail() {
    return email;
  }

  public String getOrganization() {
    return organization;
  }

  public String getLocation() {
    return location;
  }

  public void setName(String n) {
    name = n;
  }

  public void setEMail(String e) {
    email = e;
  }

  public void setOrganization(String o) {
    organization = o;
  }

  public void setLocation(String l) {
    location = l;
  }

  // toString
  public String toString() {
    return "name : " + name + "\ne-mail : " + email + "\norganization : "
            + organization + "\nlocation : " + location + "\n";
  }
}
