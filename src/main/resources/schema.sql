create schema if not EXISTS secretesite;

use secretesite;

create table if NOT EXISTS  secretesite.gene
(
	gene_id int not null auto_increment
		primary key,
	chromosome varchar(255) null,
	gene_accession varchar(255) null,
	gene_name varchar(255) null,
	gene_sequence varchar(255) null,
	constraint UK_duf8bs0xqyrniee79kkotrfi6
		unique (gene_accession)
)
;

create table if NOT EXISTS secretesite.hibernate_sequence
(
	next_val bigint null
)
;

create table if NOT EXISTS secretesite.species
(
	speciesId int not null auto_increment
		primary key,
	species_name varchar(255) null,
	species_taxonomy_number int null,
	constraint UK_n5oyr7dvw5gdnb1q5rchik4nf
		unique (species_taxonomy_number)
)
;

create table if NOT EXISTS secretesite.transcript
(
	transcriptId int not null auto_increment
		primary key,
	ensemble_transcript_accession varchar(255) null,
	sequenceEnd int null,
	sequenceStart int null,
	transcriptSequence varchar(12200) null,
	gene_id int not null,
	index transcript (gene_id)
)
;


create table if NOT EXISTS secretesite.transcript_early_folding
(
	transcript_early_folding_id int not null auto_increment
		primary key,
	folding_location int not null,
	transcript int not null,
	index transcript_early_folding (transcript)

)
;


create table if NOT EXISTS secretesite.transcript_structure
(
	transcriptstructure_id int not null
		primary key,
	chain varchar(255) null,
	fragment_end int null,
	fragment_start int null,
	identity_score double null,
	number_of_matched_residues int null,
	pdb_id varchar(255) null
)
;

create table if NOT EXISTS secretesite.transcripts_expressable_in_species
(
	transcriptId int not null,
	speciesId int not null,
	primary key (transcriptId, speciesId)
)
;

create table if NOT EXISTS secretesite.transcripts_found_in_structure
(
	id int not null
		primary key,
	cluster_representative varchar(255) null,
	l_transcript_id int null,
	l_transcriptstructure_id int null,
	index transcripts_found_in_structure_transcript_id (l_transcript_id),
	index transcripts_found_in_structure_transcript_structure_id (l_transcriptstructure_id)
)
;
