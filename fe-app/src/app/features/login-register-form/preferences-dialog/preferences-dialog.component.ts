import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-preferences-dialog',
  templateUrl: './preferences-dialog.component.html',
  styleUrls: ['./preferences-dialog.component.scss']
})
export class PreferencesDialogComponent implements OnInit {

  preferences: Array<string> = [
    "OUTDOOR_EVENTS",
    "CULTURAL_EVENTS",
    "FAMILY_EVENTS",
    "SPORTING_EVENTS",
    "RELAXING_EVENTS",
    "ADVENTURE_EVENTS",
    "ARTISTIC_EVENTS",
    "EXOTIC_EVENTS",
    "MUSIC_EVENTS",
  ]
  constructor(public dialogRef: MatDialogRef<PreferencesDialogComponent>) {}

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
  }

}
