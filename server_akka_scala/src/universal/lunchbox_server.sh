#!/bin/sh

BASEDIR=$(dirname $0)
( cd $BASEDIR/bin && `./lunchbox_server > /dev/null 2>&1` & )
