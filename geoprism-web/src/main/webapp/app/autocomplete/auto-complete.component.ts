import {
  Component,
  ElementRef,
  Input,
  Output,
  OnInit,
  ViewEncapsulation,
  EventEmitter,
  ViewChild
} from "@angular/core";
import { AutoComplete } from "./auto-complete";

/**
 * show a selected date in monthly calendar
 * Each filteredList item has the following property in addition to data itself
 *   1. displayValue as string e.g. Allen Kim
 *   2. dataValue as any e.g.
 */
@Component({
  moduleId: module.id,	
  selector: "auto-complete",
  providers: [AutoComplete],
  templateUrl: 'auto-complete.component.jsp',
  styleUrls: ['auto-complete.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AutoCompleteComponent implements OnInit {

  /**
   * public input properties
   */
  @Input("list-formatter") listFormatter: (arg: any) => string;
  @Input("source") source: any;
  @Input("path-to-data") pathToData: string;
  @Input("min-chars") minChars: number = 0;
  @Input("value-property-name") valuePropertyName: string = "id";
  @Input("display-property-name") displayPropertyName: string = "value";
  @Input("placeholder") placeholder: string;
  @Input("blank-option-text") blankOptionText: string;
  @Input("no-match-found-text") noMatchFoundText: string;
  @Input("accept-user-input") acceptUserInput: boolean;
  @Input("max-num-list") maxNumList: number;

  @Output() valueSelected = new EventEmitter();
  @Output() inputChanged = new EventEmitter();
  
  @ViewChild('autoCompleteInput') autoCompleteInput: ElementRef;

  el: HTMLElement;           // this component  element `<auto-complete>`
  inputEl: HTMLInputElement; // `<input>` element in `<auto-complete>` for auto complete
  userInputEl: any;      // directive element that called this element `<input auto-complete>`
  userInputElTabIndex: any;

  closeToBottom: boolean = false;
  dropdownVisible: boolean = false;
  isLoading: boolean = false;

  filteredList: any[] = [];
  minCharsEntered: boolean = false;
  itemIndex: number = 0;
  keyword: string;

  isSrcArr(): boolean {
    return (this.source.constructor.name === "Array");
  }

  /**
   * constructor
   */
  constructor(
    elementRef: ElementRef,
    public autoComplete: AutoComplete
  ) {
    this.el = elementRef.nativeElement;
  }

  /**
   * user enters into input el, shows list to select, then select one
   */
  ngOnInit(): void {
    this.inputEl = <HTMLInputElement>(this.el.querySelector("input"));
    this.userInputEl = this.el.parentElement.querySelector("input");
    this.autoComplete.source = this.source;
    this.autoComplete.pathToData = this.pathToData;
  }

  reloadListInDelay(): void {
    let delayMs = this.isSrcArr() ? 10 : 500;
    let keyword = this.inputEl.value;

    // executing after user stopped typing
    this.delay(() => this.reloadList(keyword), delayMs);
    this.inputChanged.emit(keyword);
  }

  showDropdownList(): void {
    this.keyword = this.userInputEl.value;
    this.inputEl.style.display = '';
    this.inputEl.focus();

    this.userInputElTabIndex = this.userInputEl['tabIndex'];
    this.userInputEl['tabIndex'] = -100;  //disable tab focus for <shift-tab> pressed

    this.reloadList(this.keyword);
  }

  hideDropdownList(): void {
    this.inputEl.style.display = 'none';
    this.dropdownVisible = false;
    this.userInputEl['tabIndex'] = this.userInputElTabIndex; // enable tab focus
  }

  reloadList(keyword: string): void {

    this.filteredList = [];
    if (keyword.length < (this.minChars || 0)) {
      this.minCharsEntered = false;
      return;
    } else {
      this.minCharsEntered = true;
    }

    this.dropdownVisible = true;

    if (this.isSrcArr()) {    // local source
      this.isLoading = false;
      this.filteredList = this.autoComplete.filter(this.source, this.keyword);
      if (this.maxNumList) {
        this.filteredList = this.filteredList.slice(0, this.maxNumList);
      }
    } else {                 // remote source
      this.isLoading = true;

      if (typeof this.source === "function") {
        // custom function that returns observable
        this.source(keyword).subscribe(
          (resp:any) => {

            if (this.pathToData) {
              let paths = this.pathToData.split(".");
              paths.forEach(prop => resp = resp[prop]);
            }

            this.filteredList = resp;
            if (this.maxNumList) {
              this.filteredList = this.filteredList.slice(0, this.maxNumList);
            }
          },
          (error:any) => {
            
          },
          () => this.isLoading = false // complete
        );
      } else {
        // remote source

        this.autoComplete.getRemoteData(keyword).subscribe(resp => {
            this.filteredList = (<any>resp);
            if (this.maxNumList) {
              this.filteredList = this.filteredList.slice(0, this.maxNumList);
            }
          },
          error => null,
          () => this.isLoading = false // complete
        );
      }
    }
  }

  selectOne(data: any) {
    this.hideDropdownList();
    this.valueSelected.emit(data);
  };

  inputElKeyHandler(evt: any) {
    let totalNumItem = this.filteredList.length;

    switch (evt.keyCode) {
      case 27: // ESC, hide auto complete
        this.hideDropdownList();
        break;

      case 38: // UP, select the previous li el
        this.itemIndex = (totalNumItem + this.itemIndex - 1) % totalNumItem;
        break;

      case 40: // DOWN, select the next li el or the first one
        this.dropdownVisible = true;
        this.itemIndex = (totalNumItem + this.itemIndex + 1) % totalNumItem;
        break;

      case 13: // ENTER, choose it!!
        if (this.filteredList.length > 0) {
          this.selectOne(this.filteredList[this.itemIndex]);
        }
        evt.preventDefault();
        break;
    }
  };

  getFormattedList(data: any): string {
    let formatter = this.listFormatter || this.defaultListFormatter;
    return formatter.apply(this, [data]);
  }

  private defaultListFormatter(data: any): string {
    let html: string = "";
    html += data[this.valuePropertyName] ? `<b>(${data[this.valuePropertyName]})</b>` : "";
    html += data[this.displayPropertyName] ? `<span>${data[this.displayPropertyName]}</span>` : data;
    return html;
  }

  private delay = (function () {
    let timer = 0;
    return function (callback: any, ms: number) {
      clearTimeout(timer);
      timer = setTimeout(callback, ms);
    };
  })();

}