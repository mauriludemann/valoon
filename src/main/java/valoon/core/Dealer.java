package valoon.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("DEALER")
@Table(name = "dealer")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dealer extends ValoonUser { }
