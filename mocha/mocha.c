/**CFile***********************************************************************

  FileName    [mocha.c]

  PackageName [mocha]

  Synopsis    [Package for dealing with Mocha reactive modules.]

  Author      [Sebastian Gfeller]

  Copyright   [Copyright (c) 2008 Ecole Polytéchnique Fédérale de Lausanne
  All rights reserved.]

******************************************************************************/

#include "mochaInt.h"

static char rcsid[] UNUSED = "$Id: tst.c,v 1.6 2002/09/10 06:14:37 fabio Exp $";

/*---------------------------------------------------------------------------*/
/* Constant declarations                                                     */
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
/* Structure declarations                                                    */
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
/* Type declarations                                                         */
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
/* Variable declarations                                                     */
/*---------------------------------------------------------------------------*/

/*---------------------------------------------------------------------------*/
/* Macro declarations                                                        */
/*---------------------------------------------------------------------------*/

/**AutomaticStart*************************************************************/

/*---------------------------------------------------------------------------*/
/* Static function prototypes                                                */
/*---------------------------------------------------------------------------*/

static int CommandReadMocha(Hrc_Manager_t ** hmgr, int argc, char ** argv);

/**AutomaticEnd***************************************************************/


/*---------------------------------------------------------------------------*/
/* Definition of exported functions                                          */
/*---------------------------------------------------------------------------*/

/**Function********************************************************************

  Synopsis    [Initializes the mocha package.]

  SideEffects []

  SeeAlso     [Mocha_End]

******************************************************************************/
void
Mocha_Init(void)
{
  /*
   * Add a command to the global command table.
   */
  Cmd_CommandAdd("read_mocha", CommandReadMocha, /* doesn't changes_network */ 0);
}


/**Function********************************************************************

  Synopsis    [Ends the mocha package.]

  SideEffects []

  SeeAlso     [Mocha_Init]

******************************************************************************/
void
Mocha_End(void)
{
  /*
   * For example, free any global memory (if any) which the test package is
   * responsible for.
   */
}


/*---------------------------------------------------------------------------*/
/* Definition of internal functions                                          */
/*---------------------------------------------------------------------------*/


/*---------------------------------------------------------------------------*/
/* Definition of static functions                                            */
/*---------------------------------------------------------------------------*/

/**Function********************************************************************

  Synopsis    [Implements the read_mocha command.]

  CommandName [read_mocha]

  CommandSynopsis [template for implementing commands]

  CommandArguments [infile.rm]
  
  CommandDescription [Given an input file, this command translates the file
  to mv format and then calls read_blif_mv on the temporary file.<p>

  ]

  SideEffects []

******************************************************************************/
static int
CommandReadMocha(
  Hrc_Manager_t ** hmgr,
  int  argc,
  char ** argv)
{
  char* inputfilename;
  int            c;
  int            verbose = 0;              /* default value */

  /*
   * Parse command line options.
   * This is just here as placeholder: at the moment we have no options.
   */
  util_getopt_reset();
  while ((c = util_getopt(argc, argv, "")) != EOF) {
    switch(c) {
      default:
        goto usage;
    }
  }

  if( argc > 1 ) { // We have an inputfile as argument.
    inputfilename = argv[1];
    char outFileName[] = "/tmp/vis.XXXXXX";
    size_t outfilelen = (sizeof(char))*strlen(outFileName);
    size_t command_length = (sizeof(char) *(
			     strlen(inputfilename) + 1 +
			     strlen(outFileName) +
			     strlen("mocha2mv ")));
    char* command = malloc(command_length);
    sprintf(command,"mocha2mv %s %s",inputfilename,outFileName);
    int status = system(command);

    char* readblifcommand = malloc(
      (sizeof(char))*strlen("read_blif_mv ") +
      outfilelen + 1);
    if( status != 0 ) {
      fprintf(vis_stderr, "Error no. %d executing mocha2mv\n", status);
    } else {
      // Now we might want to call read_blif_mv on this
      sprintf(readblifcommand,"read_blif_mv %s",outFileName);
      Cmd_CommandExecute(hmgr,readblifcommand);
    }

    // Cleanup
    free(command);
    command = NULL;
    free(readblifcommand);
    readblifcommand = NULL;
  } else {
    goto usage;
  }

  return 0;		/* normal exit */

  usage:
  (void) fprintf(vis_stderr, "usage: read_mocha inputfile.rm\n");
  (void) fprintf(vis_stderr, "   Where inputfile.rm is a module specification file.\n");
  return 1;		/* error exit */
}




