import {Directive, ComponentFactoryResolver, Input, ComponentRef, Output, EventEmitter, OnInit, ViewContainerRef, ElementRef} from "@angular/core";
import { ControlContainer, AbstractControl, FormGroup, FormControl } from "@angular/forms";

import { AutoCompleteComponent } from "./auto-complete.component";

/**
 * display auto-complete section with input and dropdown list when it is clicked
 */
@Directive({
  selector: "[auto-complete], [auto-complete]",
  host: {
    "(keyup)": "onKey($event)" // Liten to keyup events on the host component
  }
})
export class AutoCompleteDirective implements OnInit {

  // The search function should be passed as an input
  @Input() public source: (term: string) => Promise<Array<{ text: string, data: any }>>;
    
  // The directive emits onDropdownSelect event when an item from the list is selected
  @Output() public onDropdownSelect = new EventEmitter();

  private term = "";
  
  private listCmp: ComponentRef<AutoCompleteComponent> = undefined;
  private refreshTimer: any = undefined;
  private searchInProgress = false;
  private searchRequired = false;

  constructor(
    private resolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef,
    private el: ElementRef) { }
  
  public ngOnInit() {
    
    // When an item is selected remove the list
    this.onDropdownSelect.subscribe(() => {
      this.removeList();
    });
  }  
  
  /** 
   * On key event is triggered when a key is released on the host component
   * the event starts a timer to prevent concurrent requests
   */
  public onKey(event: any) {
  // Arrow down code
    if(event.keyCode === 40) {
      event.preventDefault();
    
      if (this.listCmp) {
        (<AutoCompleteComponent>(this.listCmp.instance)).down();
      }
    }
    // Arrow up keycode
    else if(event.keyCode === 38) {
      if (this.listCmp) {
        (<AutoCompleteComponent>(this.listCmp.instance)).up();
      }    
    }
    // Enter keycode
    else if(event.keyCode === 13) {
      if (this.listCmp) {
        (<AutoCompleteComponent>(this.listCmp.instance)).enter();
      }
    }  
    else if (!this.refreshTimer) {
      this.refreshTimer = setTimeout(() => {
        if (!this.searchInProgress) {
          this.doSearch();
        } else {
          // If a request is in progress mark that a new search is required
          this.searchRequired = true;
        }
      },200);
    }
    
    this.term = event.target.value;
    
    if (this.term === "" && this.listCmp) {
      // clean the list if the search term is empty
      this.removeList();
    }
  }

  /**
   * Call the search function and handle the results
   */
  private doSearch() {
    
    this.refreshTimer = undefined;
    
    // if we have a search function and a valid search term call the search
    if (this.source && this.term !== ""  && this.term.length > 1) {
      this.searchInProgress = true;
      
      this.source(this.term).then((res) => {
        this.searchInProgress = false;
        
        // if the term has changed during our search do another search
        if (this.searchRequired) {
          this.searchRequired = false;
          this.doSearch();
        } else {
          // display the list of results
          this.displayList(res);
        }
      })
      .catch(err => {
        
        this.removeList();
      });
    }
    else if(this.term === "") {
      this.onDropdownSelect.emit({ text: '', data: null });      
    }
  }

  /**
   * Display the list of results
   * Dynamically load the list component if it doesn't exist yet and update the suggestions list
   */
  private displayList(list: Array<{ text: string, data: any }>) {
    if (!this.listCmp) {
      
      let factory = this.resolver.resolveComponentFactory(AutoCompleteComponent);

      this.listCmp = this.viewContainerRef.createComponent(factory);      
      
      this.updateList(list);
        
      // Emit the selectd event when the component fires its selected event
      (<AutoCompleteComponent>(this.listCmp.instance)).onDropdownSelect.subscribe((selectedItem: {text: string, data: any}) => {        
      this.el.nativeElement.value = selectedItem.text;
      
        this.onDropdownSelect.emit(selectedItem);
      });      
    } else {
      this.updateList(list);
    }
  }

  /**
   * Update the suggestions list in the list component
   */
  private updateList(list: Array<{ text: string, data: any }>) {
    if (this.listCmp) {
      (<AutoCompleteComponent>(this.listCmp.instance)).setOptions(list);
    }
  }

  /**
   * remove the list component
   */
  private removeList() {
    this.searchInProgress = false;
    this.searchRequired = false;
    
    if (this.listCmp) {
      this.listCmp.destroy();
      this.listCmp = undefined;
    }
  }
}