import { Injectable } from '@angular/core';
import { Subject }    from 'rxjs/Subject';

@Injectable()
export class NavigationService {
	
  // Observable string sources
  private navigationSource = new Subject<string>();
  
  // Observable string streams
  navigationAnnounced$ = this.navigationSource.asObservable();

  // Service message commands
  navigate(direction: string) {
    this.navigationSource.next(direction);
  }
}