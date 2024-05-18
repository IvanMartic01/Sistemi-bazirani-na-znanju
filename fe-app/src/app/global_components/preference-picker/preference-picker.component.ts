import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-preference-picker',
  templateUrl: './preference-picker.component.html',
  styleUrls: ['./preference-picker.component.scss']
})
export class PreferencePickerComponent implements OnInit {

  @Input() preferences: Array<{ preference: string, checked: boolean }> = [];
  @Input() preferencesOpened: boolean = false;
  @Output() closingEventEmitter: EventEmitter<boolean>= new EventEmitter<boolean>();

  constructor() { }

  ngOnInit(): void {
  }

  closePreferencesWindow() {
    this.preferencesOpened = false;
    this.closingEventEmitter.emit(this.preferencesOpened);
  }

  formatPreference(preference: string) {
    return preference.replaceAll("_", " ");
  }

  checkPreference(preference: { preference: string, checked: boolean}) {
    preference.checked = !preference.checked;
  }

}
