

public enum ConfigMessageType {

	// from RM to GS
	checkalive,
	echoalive,

	// from GS to RM
	recon,

	// both ways
	AddJob,
	crash
}
